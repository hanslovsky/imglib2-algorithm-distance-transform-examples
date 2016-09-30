package de.hanslovsky.examples.distance;

import net.imglib2.algorithm.morphology.distance.DistanceTransform;
import net.imglib2.img.array.ArrayImg;
import net.imglib2.img.array.ArrayImgs;
import net.imglib2.img.basictypeaccess.array.FloatArray;
import net.imglib2.type.numeric.real.FloatType;
import net.imglib2.view.Views;

public class DistanceTransformTest2
{

	public static void main( final String[] args )
	{
		final float f[] = { 1.0f, 2.0f, 3.0f, 4.0f, 1.0f, 10.0f, 5.0f };
		final ArrayImg< FloatType, FloatArray > img = ArrayImgs.floats( f, 1, f.length );
		final ArrayImg< FloatType, FloatArray > target = ArrayImgs.floats( 1, f.length );

		DistanceTransform.transformL1_1D( Views.collapseReal( img ).randomAccess().get(), Views.collapseReal( target ).randomAccess().get(), 1.0, f.length );

		for ( final FloatType i : img )
			System.out.print( i + ", " );
		System.out.println();

		for ( final FloatType i : target )
			System.out.print( i + ", " );
		System.out.println();

	}

}
