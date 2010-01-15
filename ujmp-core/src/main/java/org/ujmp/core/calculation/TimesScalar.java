/*
 * Copyright (C) 2008-2010 by Holger Arndt
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

package org.ujmp.core.calculation;

import java.math.BigDecimal;

import org.ujmp.core.Matrix;
import org.ujmp.core.doublematrix.DenseDoubleMatrix2D;
import org.ujmp.core.interfaces.HasDoubleArray;
import org.ujmp.core.interfaces.HasDoubleArray2D;
import org.ujmp.core.matrix.DenseMatrix;
import org.ujmp.core.matrix.DenseMatrix2D;
import org.ujmp.core.matrix.SparseMatrix;
import org.ujmp.core.util.MathUtil;
import org.ujmp.core.util.UJMPSettings;
import org.ujmp.core.util.concurrent.PForEquidistant;

public interface TimesScalar<T> {

	public static TimesScalar<Matrix> MATRIX = new TimesScalar<Matrix>() {

		public void calc(final Matrix source, final BigDecimal factor, final Matrix target) {
			if (source instanceof DenseMatrix && target instanceof DenseMatrix) {
				TimesScalar.DENSEMATRIX.calc((DenseMatrix) source, factor, (DenseMatrix) target);
			} else if (source instanceof SparseMatrix && target instanceof SparseMatrix) {
				TimesScalar.SPARSEMATRIX.calc((SparseMatrix) source, factor, (SparseMatrix) target);
			} else {
				for (long[] c : source.allCoordinates()) {
					BigDecimal value = source.getAsBigDecimal(c);
					BigDecimal result = MathUtil.times(value, factor);
					target.setAsBigDecimal(result, c);
				}
			}
		}

		public void calc(final Matrix source, final double factor, final Matrix target) {
			if (source instanceof DenseMatrix && target instanceof DenseMatrix) {
				TimesScalar.DENSEMATRIX.calc((DenseMatrix) source, factor, (DenseMatrix) target);
			} else if (source instanceof SparseMatrix && target instanceof SparseMatrix) {
				TimesScalar.SPARSEMATRIX.calc((SparseMatrix) source, factor, (SparseMatrix) target);
			} else {
				calc(source, new BigDecimal(factor, MathUtil.getDefaultMathContext()), target);
			}
		}
	};

	public static TimesScalar<DenseMatrix> DENSEMATRIX = new TimesScalar<DenseMatrix>() {

		public void calc(final DenseMatrix source, final BigDecimal factor, final DenseMatrix target) {
			if (source instanceof DenseMatrix2D && target instanceof DenseMatrix2D) {
				TimesScalar.DENSEMATRIX2D.calc((DenseMatrix2D) source, factor,
						(DenseMatrix2D) target);
			} else {
				for (long[] c : source.allCoordinates()) {
					BigDecimal value = source.getAsBigDecimal(c);
					BigDecimal result = MathUtil.times(value, factor);
					target.setAsBigDecimal(result, c);
				}
			}
		}

		public void calc(final DenseMatrix source, final double factor, final DenseMatrix target) {
			if (source instanceof DenseMatrix2D && target instanceof DenseMatrix2D) {
				TimesScalar.DENSEMATRIX2D.calc((DenseMatrix2D) source, factor,
						(DenseMatrix2D) target);
			} else {
				calc(source, new BigDecimal(factor, MathUtil.getDefaultMathContext()), target);
			}
		}
	};

	public static TimesScalar<SparseMatrix> SPARSEMATRIX = new TimesScalar<SparseMatrix>() {

		public void calc(final SparseMatrix source, final BigDecimal factor,
				final SparseMatrix target) {
			for (long[] c : source.availableCoordinates()) {
				BigDecimal value = source.getAsBigDecimal(c);
				BigDecimal result = MathUtil.times(value, factor);
				target.setAsBigDecimal(result, c);
			}
		}

		public void calc(final SparseMatrix source, final double factor, final SparseMatrix target) {
			calc(source, new BigDecimal(factor, MathUtil.getDefaultMathContext()), target);
		}
	};

	public static TimesScalar<DenseMatrix2D> DENSEMATRIX2D = new TimesScalar<DenseMatrix2D>() {

		public void calc(final DenseMatrix2D source, final BigDecimal factor,
				final DenseMatrix2D target) {
			if (source instanceof DenseDoubleMatrix2D && target instanceof DenseDoubleMatrix2D) {
				TimesScalar.DENSEDOUBLEMATRIX2D.calc((DenseDoubleMatrix2D) source, factor,
						(DenseDoubleMatrix2D) target);
			} else {
				for (int r = (int) source.getRowCount(); --r != -1;) {
					for (int c = (int) source.getColumnCount(); --c != -1;) {
						BigDecimal value = source.getAsBigDecimal(r, c);
						BigDecimal result = MathUtil.times(value, factor);
						target.setAsBigDecimal(result, r, c);
					}
				}
			}
		}

		public void calc(final DenseMatrix2D source, final double factor, final DenseMatrix2D target) {
			if (source instanceof DenseDoubleMatrix2D && target instanceof DenseDoubleMatrix2D) {
				TimesScalar.DENSEDOUBLEMATRIX2D.calc((DenseDoubleMatrix2D) source, factor,
						(DenseDoubleMatrix2D) target);
			} else {
				calc(source, new BigDecimal(factor, MathUtil.getDefaultMathContext()), target);
			}
		}
	};

	public static TimesScalar<DenseDoubleMatrix2D> DENSEDOUBLEMATRIX2D = new TimesScalar<DenseDoubleMatrix2D>() {

		public void calc(final DenseDoubleMatrix2D source, final BigDecimal factor,
				final DenseDoubleMatrix2D target) {
			calc(source, factor.doubleValue(), target);
		}

		public void calc(final DenseDoubleMatrix2D source, final double factor,
				final DenseDoubleMatrix2D target) {
			if (source instanceof HasDoubleArray2D && target instanceof HasDoubleArray2D) {
				calc(((HasDoubleArray2D) source).getDoubleArray2D(), factor,
						((HasDoubleArray2D) target).getDoubleArray2D());
			} else if (source instanceof HasDoubleArray && target instanceof HasDoubleArray) {
				calc(((HasDoubleArray) source).getDoubleArray(), factor, ((HasDoubleArray) target)
						.getDoubleArray());
			} else {
				for (int r = (int) source.getRowCount(); --r != -1;) {
					for (int c = (int) source.getColumnCount(); --c != -1;) {
						target.setDouble(factor * source.getDouble(r, c), r, c);
					}
				}
			}
		}

		private void calc(final double[][] source, final double factor, final double[][] target) {
			final int rows = source.length;
			final int cols = source[0].length;
			if (UJMPSettings.getNumberOfThreads() > 1 && rows >= 100 && cols >= 100) {
				new PForEquidistant(0, rows - 1) {

					public void step(int i) {
						double[] tsource = source[i];
						double[] ttarget = target[i];
						for (int c = 0; c < cols; c++) {
							ttarget[c] = tsource[c] * factor;
						}
					}
				};
			} else {
				double[] tsource = null;
				double[] ttarget = null;
				for (int r = 0; r < rows; r++) {
					tsource = source[r];
					ttarget = target[r];
					for (int c = 0; c < cols; c++) {
						ttarget[c] = tsource[c] * factor;
					}
				}
			}
		}

		private void calc(final double[] source, final double factor, final double[] target) {
			final int length = source.length;
			for (int i = 0; i < length; i++) {
				target[i] = factor * source[i];
			}
		}
	};

	public void calc(final T source, final BigDecimal factor, final T target);

	public void calc(final T source, final double factor, final T target);

}
