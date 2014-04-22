/*
 * Copyright (C) 2008-2014 by Holger Arndt
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

package org.ujmp.core.doublematrix.calculation.entrywise.hyperbolic;

import org.ujmp.core.Matrix;
import org.ujmp.core.doublematrix.calculation.AbstractDoubleCalculation;

public class Sinh extends AbstractDoubleCalculation {
	private static final long serialVersionUID = 2083545526665186477L;

	public Sinh(Matrix matrix) {
		super(matrix);
	}

	public double getDouble(long... coordinates) {
		return Math.sinh(getSource().getAsDouble(coordinates));
	}

	public static Matrix calc(Matrix source) {
		Matrix ret = Matrix.Factory.zeros(source.getSize());
		for (long[] c : source.availableCoordinates()) {
			ret.setAsDouble(Math.sinh(source.getAsDouble(c)), c);
		}
		return ret;
	}

}
