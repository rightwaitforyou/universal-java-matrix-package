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

public interface DivideScalar<T> {

	public static DivideScalar<Matrix> MATRIX = new DivideScalar<Matrix>() {

		public void calc(final Matrix source, final BigDecimal divisor, final Matrix target) {
			if (source instanceof DenseMatrix && target instanceof DenseMatrix) {
				DivideScalar.DENSEMATRIX.calc((DenseMatrix) source, divisor, (DenseMatrix) target);
			} else if (source instanceof SparseMatrix && target instanceof SparseMatrix) {
				DivideScalar.SPARSEMATRIX.calc((SparseMatrix) source, divisor,
						(SparseMatrix) target);
			} else {
				for (long[] c : source.allCoordinates()) {
					BigDecimal value = source.getAsBigDecimal(c);
					BigDecimal result = MathUtil.divide(value, divisor);
					target.setAsBigDecimal(result, c);
				}
			}
		}

		public void calc(final Matrix source, final double divisor, final Matrix target) {
			if (source instanceof DenseMatrix && target instanceof DenseMatrix) {
				DivideScalar.DENSEMATRIX.calc((DenseMatrix) source, divisor, (DenseMatrix) target);
			} else if (source instanceof SparseMatrix && target instanceof SparseMatrix) {
				DivideScalar.SPARSEMATRIX.calc((SparseMatrix) source, divisor,
						(SparseMatrix) target);
			} else {
				calc(source, new BigDecimal(divisor, MathUtil.getDefaultMathContext()), target);
			}
		}
	};

	public static DivideScalar<DenseMatrix> DENSEMATRIX = new DivideScalar<DenseMatrix>() {

		public void calc(final DenseMatrix source, final BigDecimal divisor,
				final DenseMatrix target) {
			if (source instanceof DenseMatrix2D && target instanceof DenseMatrix2D) {
				DivideScalar.DENSEMATRIX2D.calc((DenseMatrix2D) source, divisor,
						(DenseMatrix2D) target);
			} else {
				for (long[] c : source.allCoordinates()) {
					BigDecimal value = source.getAsBigDecimal(c);
					BigDecimal result = MathUtil.divide(value, divisor);
					target.setAsBigDecimal(result, c);
				}
			}
		}

		public void calc(final DenseMatrix source, final double divisor, final DenseMatrix target) {
			if (source instanceof DenseMatrix2D && target instanceof DenseMatrix2D) {
				DivideScalar.DENSEMATRIX2D.calc((DenseMatrix2D) source, divisor,
						(DenseMatrix2D) target);
			} else {
				calc(source, new BigDecimal(divisor, MathUtil.getDefaultMathContext()), target);
			}
		}
	};

	public static DivideScalar<SparseMatrix> SPARSEMATRIX = new DivideScalar<SparseMatrix>() {

		public void calc(final SparseMatrix source, final BigDecimal divisor,
				final SparseMatrix target) {
			for (long[] c : source.availableCoordinates()) {
				BigDecimal value = source.getAsBigDecimal(c);
				BigDecimal result = MathUtil.divide(value, divisor);
				target.setAsBigDecimal(result, c);
			}
		}

		public void calc(SparseMatrix source, double divisor, SparseMatrix target) {
			calc(source, new BigDecimal(divisor, MathUtil.getDefaultMathContext()), target);
		}
	};

	public static DivideScalar<DenseMatrix2D> DENSEMATRIX2D = new DivideScalar<DenseMatrix2D>() {

		public void calc(final DenseMatrix2D source, final BigDecimal divisor,
				final DenseMatrix2D target) {
			if (source instanceof DenseDoubleMatrix2D && target instanceof DenseDoubleMatrix2D) {
				DivideScalar.DENSEDOUBLEMATRIX2D.calc((DenseDoubleMatrix2D) source, divisor,
						(DenseDoubleMatrix2D) target);
			} else {
				for (int r = (int) source.getRowCount(); --r != -1;) {
					for (int c = (int) source.getColumnCount(); --c != -1;) {
						BigDecimal value = source.getAsBigDecimal(r, c);
						BigDecimal result = MathUtil.divide(value, divisor);
						target.setAsBigDecimal(result, r, c);
					}
				}
			}
		}

		public void calc(final DenseMatrix2D source, final double divisor,
				final DenseMatrix2D target) {
			if (source instanceof DenseDoubleMatrix2D && target instanceof DenseDoubleMatrix2D) {
				DivideScalar.DENSEDOUBLEMATRIX2D.calc((DenseDoubleMatrix2D) source, divisor,
						(DenseDoubleMatrix2D) target);
			} else {
				calc(source, new BigDecimal(divisor, MathUtil.getDefaultMathContext()), target);
			}
		}
	};

	public static DivideScalar<DenseDoubleMatrix2D> DENSEDOUBLEMATRIX2D = new DivideScalar<DenseDoubleMatrix2D>() {

		public void calc(DenseDoubleMatrix2D source, BigDecimal divisor, DenseDoubleMatrix2D target) {
			calc(source, divisor.doubleValue(), target);
		}

		public void calc(final DenseDoubleMatrix2D source, final double divisor,
				final DenseDoubleMatrix2D target) {
			if (source instanceof HasDoubleArray2D && target instanceof HasDoubleArray2D) {
				calc(((HasDoubleArray2D) source).getDoubleArray2D(), divisor,
						((HasDoubleArray2D) target).getDoubleArray2D());
			} else if (source instanceof HasDoubleArray && target instanceof HasDoubleArray) {
				calc(((HasDoubleArray) source).getDoubleArray(), divisor, ((HasDoubleArray) target)
						.getDoubleArray());
			} else {
				for (int r = (int) source.getRowCount(); --r != -1;) {
					for (int c = (int) source.getColumnCount(); --c != -1;) {
						target.setDouble(source.getDouble(r, c) / divisor, r, c);
					}
				}
			}
		}

		private void calc(final double[][] source, final double divisor, final double[][] target) {
			if (UJMPSettings.getNumberOfThreads() > 1 && source.length >= 100
					&& source[0].length >= 100) {
				new PForEquidistant(0, source.length - 1) {
					public void step(int i) {
						double[] tsource = source[i];
						double[] ttarget = target[i];
						for (int c = source[0].length; --c != -1;) {
							ttarget[c] = tsource[c] / divisor;
						}
					}
				};
			} else {
				double[] tsource = null;
				double[] ttarget = null;
				for (int r = source.length; --r != -1;) {
					tsource = source[r];
					ttarget = target[r];
					for (int c = source[0].length; --c != -1;) {
						ttarget[c] = tsource[c] / divisor;
					}
				}
			}
		}

		private void calc(final double[] source, final double divisor, final double[] target) {
			final int length = source.length;
			for (int i = 0; i < length; i++) {
				target[i] = source[i] / divisor;
			}
		}
	};

	public void calc(T source, BigDecimal divisor, T target);

	public void calc(T source, double divisor, T target);

}
