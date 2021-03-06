/*
 * Copyright (C) 2008-2015 by Holger Arndt
 *
 * This file is part of the Universal Java Matrix Package (UJMP).
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership and licensing.
 *
 * UJMP is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * UJMP is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with UJMP; if not, write to the
 * Free Software Foundation, Inc., 51 Franklin St, Fifth Floor,
 * Boston, MA  02110-1301  USA
 */

package org.ujmp.core.util;

import java.util.Random;

public class RandomSimple extends Random {
	private static final long serialVersionUID = -7454312208651558308L;

	private long x = 0;

	public RandomSimple() {
		this(System.nanoTime());
	}

	public RandomSimple(final long seed) {
		x = seed;
	}

	public final long nextLong() {
		x ^= (x << 21);
		x ^= (x >>> 35);
		x ^= (x << 4);
		return x;
	}

	@Override
	protected final int next(final int bits) {
		return (int) (nextLong() >>> (64 - bits));
	}

	public synchronized void setSeed(long seed) {
		this.x = seed;
		super.setSeed(seed);
	}

}
