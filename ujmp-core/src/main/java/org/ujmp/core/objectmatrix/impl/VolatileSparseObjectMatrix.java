/*
 * Copyright (C) 2008-2009 Holger Arndt, A. Naegele and M. Bundschus
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

package org.ujmp.core.objectmatrix.impl;

import java.util.Map;

import org.ujmp.core.Matrix;
import org.ujmp.core.collections.SoftHashMap;
import org.ujmp.core.coordinates.CoordinateIterator;
import org.ujmp.core.coordinates.CoordinateSetToLongWrapper;
import org.ujmp.core.coordinates.Coordinates;
import org.ujmp.core.exceptions.MatrixException;
import org.ujmp.core.objectmatrix.stub.AbstractSparseObjectMatrix;

public class VolatileSparseObjectMatrix extends AbstractSparseObjectMatrix {
	private static final long serialVersionUID = 392817709394048419L;

	private final Map<Coordinates, Object> values = new SoftHashMap<Coordinates, Object>();

	private long[] size = null;

	public VolatileSparseObjectMatrix(Matrix m) throws MatrixException {
		this.size = Coordinates.copyOf(m.getSize());
		for (long[] c : m.allCoordinates()) {
			setAsDouble(m.getAsDouble(c), c);
		}
	}

	
	public boolean isTransient() {
		return true;
	}

	public VolatileSparseObjectMatrix(long... size) {
		this.size = Coordinates.copyOf(size);
	}

	public long[] getSize() {
		return size;
	}

	
	public Object getObject(long... coordinates) {
		return values.get(new Coordinates(coordinates));
	}

	
	public long getValueCount() {
		return values.size();
	}

	public void setObject(Object value, long... coordinates) {
		values.put(new Coordinates(coordinates), value);
	}

	public Iterable<long[]> allCoordinates() {
		return new CoordinateIterator(getSize());
	}

	public Iterable<long[]> entries() {
		return new CoordinateSetToLongWrapper(values.keySet());
	}

	public boolean contains(long... coordinates) {
		return values.containsKey(new Coordinates(coordinates));
	}

}