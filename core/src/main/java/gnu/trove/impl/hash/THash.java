/*
 * Copyright (C) 2022 RealYusufIsmail
 *
 * This library is free software; you can redistribute it and/or
 *
 * modify it under the terms of the GNU Lesser General Public
 *
 * License as published by the Free Software Foundation; either
 *
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 */ 
package gnu.trove.impl.hash;

import gnu.trove.impl.Constants;
import gnu.trove.impl.PrimeFinder;

import java.io.Externalizable;
import java.io.ObjectOutput;
import java.io.IOException;
import java.io.ObjectInput;



/**
 * Base class for hashtables that use open addressing to resolve collisions.
 *
 * Created: Wed Nov 28 21:11:16 2001
 *
 * @author Eric D. Friedman
 * @author Rob Eden (auto-compaction)
 * @author Jeff Randall
 *
 * @version $Id: THash.java,v 1.1.2.4 2010/03/02 00:55:34 robeden Exp $
 */
abstract public class THash implements Externalizable {
    @SuppressWarnings({"UnusedDeclaration"})
    static final long serialVersionUID = -1792948471915530295L;

    /** the load above which rehashing occurs. */
    protected static final float DEFAULT_LOAD_FACTOR = Constants.DEFAULT_LOAD_FACTOR;

    /**
     * the default initial capacity for the hash table. This is one less than a prime value because
     * one is added to it when searching for a prime capacity to account for the free slot required
     * by open addressing. Thus, the real default capacity is 11.
     */
    protected static final int DEFAULT_CAPACITY = Constants.DEFAULT_CAPACITY;


    /** the current number of occupied slots in the hash. */
    protected transient int _size;

    /** the current number of free slots in the hash. */
    protected transient int _free;

    /**
     * Determines how full the internal table can become before rehashing is required. This must be
     * a value in the range: 0.0 &lt; loadFactor &lt; 1.0. The default value is 0.5, which is about
     * as large as you can get in open addressing without hurting performance. Cf. Knuth, Volume 3.,
     * Chapter 6.
     */
    protected float _loadFactor;

    /**
     * The maximum number of elements allowed without allocating more space.
     */
    protected int _maxSize;


    /** The number of removes that should be performed before an auto-compaction occurs. */
    protected int _autoCompactRemovesRemaining;

    /**
     * The auto-compaction factor for the table.
     *
     * @see #setAutoCompactionFactor
     */
    protected float _autoCompactionFactor;

    /** @see #tempDisableAutoCompaction */
    protected transient boolean _autoCompactTemporaryDisable = false;


    /**
     * Creates a new <code>THash</code> instance with the default capacity and load factor.
     */
    public THash() {
        this(DEFAULT_CAPACITY, DEFAULT_LOAD_FACTOR);
    }


    /**
     * Creates a new <code>THash</code> instance with a prime capacity at or near the specified
     * capacity and with the default load factor.
     *
     * @param initialCapacity an <code>int</code> value
     */
    public THash(int initialCapacity) {
        this(initialCapacity, DEFAULT_LOAD_FACTOR);
    }

    /**
     * Creates a new <code>THash</code> instance with a prime capacity at or near the minimum needed
     * to hold <kbd>initialCapacity</kbd> elements with load factor <kbd>loadFactor</kbd> without
     * triggering a rehash.
     *
     * @param initialCapacity a positive <code>int</code> value
     * @param loadFactor a positive <code>float</code>
     */
    public THash(int initialCapacity, float loadFactor) {
        super();
        if (initialCapacity < 0) {
            throw new IllegalArgumentException("negative capacity: " + initialCapacity);
        } else if (0.0f >= loadFactor) {
            throw new IllegalArgumentException("load factor out of range: " + loadFactor);
        }
        _loadFactor = loadFactor;

        // Through testing, the load factor (especially the default load factor) has been
        // found to be a pretty good starting auto-compaction factor.
        _autoCompactionFactor = loadFactor;

        // Floats have 24 significand bits, causing loss of precision for initial capacities > ~17
        // million
        setUp(saturatedCast(fastCeil(initialCapacity / (double) loadFactor)));
    }

    /*
     * In profiling, it has been found to be faster to have our own local implementation of "ceil"
     * rather than to call to {@link Math#ceil(double)}.
     *
     * precondition: v > 0
     */
    protected static long fastCeil(double v) {
        long possible_result = (long) v;
        if (v - possible_result > 0)
            possible_result++;
        return possible_result;
    }

    /* precondition: v > 0 */
    protected static int saturatedCast(long v) {
        int r = (int) (v & 0x7fffffff); // removing sign bit
        if (r != v) {
            return Integer.MAX_VALUE;
        }
        return r;
    }

    /**
     * Tells whether this set is currently holding any elements.
     *
     * @return a <code>boolean</code> value
     */
    public boolean isEmpty() {
        return 0 == _size;
    }


    /**
     * Returns the number of distinct elements in this collection.
     *
     * @return an <code>int</code> value
     */
    public int size() {
        return _size;
    }


    /** @return the current physical capacity of the hash table. */
    abstract public int capacity();


    /**
     * Ensure that this hashtable has sufficient capacity to hold <kbd>desiredCapacity</kbd>
     * <b>additional</b> elements without requiring a rehash. This is a tuning method you can call
     * before doing a large insert.
     *
     * @param desiredCapacity an <code>int</code> value
     */
    public void ensureCapacity(int desiredCapacity) {
        if (desiredCapacity > (_maxSize - size())) {
            rehash(PrimeFinder.nextPrime(Math.max(_size + 1, saturatedCast(
                    fastCeil((desiredCapacity + _size) / (double) _loadFactor) + 1))));
            if (capacity() >= PrimeFinder.largestPrime) {
                _loadFactor = 1.0f;
            }
            computeMaxSize(capacity());
        }
    }


    /**
     * Compresses the hashtable to the minimum prime size (as defined by PrimeFinder) that will hold
     * all of the elements currently in the table. If you have done a lot of <kbd>remove</kbd>
     * operations and plan to do a lot of queries or insertions or iteration, it is a good idea to
     * invoke this method. Doing so will accomplish two things: <br>
     * <ol>
     * <li>You'll free memory allocated to the table but no longer needed because of the
     * remove()s.</li>
     * <li>You'll get better query/insert/iterator performance because there won't be any
     * <kbd>REMOVED</kbd> slots to skip over when probing for indices in the table.</li>
     * </ol>
     */
    public void compact() {
        // need at least one free spot for open addressing
        rehash(PrimeFinder.nextPrime(
                Math.max(_size + 1, saturatedCast(fastCeil(_size / (double) _loadFactor) + 1))));
        computeMaxSize(capacity());

        // If auto-compaction is enabled, re-determine the compaction interval
        if (_autoCompactionFactor != 0) {
            computeNextAutoCompactionAmount(size());
        }
    }


    /**
     * The auto-compaction factor controls whether and when a table performs a {@link #compact}
     * automatically after a certain number of remove operations. If the value is non-zero, the
     * number of removes that need to occur for auto-compaction is the size of table at the time of
     * the previous compaction (or the initial capacity) multiplied by this factor. <br>
     * Setting this value to zero will disable auto-compaction.
     *
     * @param factor a <kbd>float</kbd> that indicates the auto-compaction factor
     */
    public void setAutoCompactionFactor(float factor) {
        if (factor < 0) {
            throw new IllegalArgumentException("Factor must be >= 0: " + factor);
        }

        _autoCompactionFactor = factor;
    }


    /**
     * @see #setAutoCompactionFactor
     *
     * @return a <kbd>float</kbd> that represents the auto-compaction factor.
     */
    public float getAutoCompactionFactor() {
        return _autoCompactionFactor;
    }


    /**
     * This simply calls {@link #compact compact}. It is included for symmetry with other collection
     * classes. Note that the name of this method is somewhat misleading (which is why we prefer
     * <kbd>compact</kbd>) as the load factor may require capacity above and beyond the size of this
     * collection.
     *
     * @see #compact
     */
    public final void trimToSize() {
        compact();
    }


    /**
     * Delete the record at <kbd>index</kbd>. Reduces the size of the collection by one.
     *
     * @param index an <code>int</code> value
     */
    protected void removeAt(int index) {
        _size--;

        // If auto-compaction is enabled, see if we need to compact
        if (_autoCompactionFactor != 0) {
            _autoCompactRemovesRemaining--;

            if (!_autoCompactTemporaryDisable && _autoCompactRemovesRemaining <= 0) {
                // Do the compact
                // NOTE: this will cause the next compaction interval to be calculated
                compact();
            }
        }
    }


    /** Empties the collection. */
    public void clear() {
        _size = 0;
        _free = capacity();
    }


    /**
     * initializes the hashtable to a prime capacity which is at least <kbd>initialCapacity +
     * 1</kbd>.
     *
     * @param initialCapacity an <code>int</code> value
     * @return the actual capacity chosen
     */
    protected int setUp(int initialCapacity) {
        int capacity;

        capacity = PrimeFinder.nextPrime(initialCapacity);
        if (capacity >= PrimeFinder.largestPrime) {
            _loadFactor = 1.0f;
        }
        computeMaxSize(capacity);
        computeNextAutoCompactionAmount(initialCapacity);

        return capacity;
    }


    /**
     * Rehashes the set.
     *
     * @param newCapacity an <code>int</code> value
     */
    protected abstract void rehash(int newCapacity);


    /**
     * Temporarily disables auto-compaction. MUST be followed by calling
     * {@link #reenableAutoCompaction}.
     */
    public void tempDisableAutoCompaction() {
        _autoCompactTemporaryDisable = true;
    }


    /**
     * Re-enable auto-compaction after it was disabled via {@link #tempDisableAutoCompaction()}.
     *
     * @param check_for_compaction True if compaction should be performed if needed before
     *        returning. If false, no compaction will be performed.
     */
    public void reenableAutoCompaction(boolean check_for_compaction) {
        _autoCompactTemporaryDisable = false;

        if (check_for_compaction && _autoCompactRemovesRemaining <= 0
                && _autoCompactionFactor != 0) {

            // Do the compact
            // NOTE: this will cause the next compaction interval to be calculated
            compact();
        }
    }


    /**
     * Computes the values of maxSize. There will always be at least one free slot required.
     *
     * @param capacity an <code>int</code> value
     */
    protected void computeMaxSize(int capacity) {
        // need at least one free slot for open addressing
        _maxSize = Math.min(capacity - 1, (int) (capacity * _loadFactor));
        _free = capacity - _size; // reset the free element count
    }


    /**
     * Computes the number of removes that need to happen before the next auto-compaction will
     * occur.
     *
     * @param size an <kbd>int</kbd> that sets the auto-compaction limit.
     */
    protected void computeNextAutoCompactionAmount(int size) {
        if (_autoCompactionFactor != 0) {
            // NOTE: doing the round ourselves has been found to be faster than using
            // Math.round.
            _autoCompactRemovesRemaining = (int) ((size * _autoCompactionFactor) + 0.5f);
        }
    }


    /**
     * After an insert, this hook is called to adjust the size/free values of the set and to perform
     * rehashing if necessary.
     *
     * @param usedFreeSlot the slot
     */
    protected final void postInsertHook(boolean usedFreeSlot) {
        if (usedFreeSlot) {
            _free--;
        }

        // rehash whenever we exhaust the available space in the table
        if (++_size > _maxSize || _free == 0) {
            // choose a new capacity suited to the new state of the table
            // if we've grown beyond our maximum size, double capacity;
            // if we've exhausted the free spots, rehash to the same capacity,
            // which will free up any stale removed slots for reuse.
            int newCapacity =
                    _size > _maxSize ? PrimeFinder.nextPrime(capacity() << 1) : capacity();
            rehash(newCapacity);
            computeMaxSize(capacity());
        }
    }


    protected int calculateGrownCapacity() {
        return capacity() << 1;
    }


    public void writeExternal(ObjectOutput out) throws IOException {
        // VERSION
        out.writeByte(0);

        // LOAD FACTOR
        out.writeFloat(_loadFactor);

        // AUTO COMPACTION LOAD FACTOR
        out.writeFloat(_autoCompactionFactor);
    }


    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {

        // VERSION
        in.readByte();

        // LOAD FACTOR
        float old_factor = _loadFactor;
        _loadFactor = Math.abs(in.readFloat());

        // AUTO COMPACTION LOAD FACTOR
        _autoCompactionFactor = in.readFloat();

        // If we change the laod factor from the default, re-setup
        if (old_factor != _loadFactor) {
            setUp(saturatedCast((long) Math.ceil(DEFAULT_CAPACITY / (double) _loadFactor)));
        }
    }
}// THash
