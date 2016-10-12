package de.hanslovsky.examples.distance;

import java.util.concurrent.ExecutionException;

import net.imglib2.algorithm.morphology.distance.DistanceTransform;
import net.imglib2.algorithm.morphology.distance.DistanceTransform.DISTANCE_TYPE;
import net.imglib2.img.array.ArrayImg;
import net.imglib2.img.array.ArrayImgs;
import net.imglib2.img.basictypeaccess.array.FloatArray;
import net.imglib2.type.numeric.real.FloatType;

public class DistanceTransformTest2
{

	public static void main( final String[] args ) throws InterruptedException, ExecutionException
	{
		final float f[] = { 1.0f, 2.0f, 3.0f, 4.0f, 1.0f, 10.0f, 5.0f };
		final ArrayImg< FloatType, FloatArray > img = ArrayImgs.floats( f, 1, f.length );
		final ArrayImg< FloatType, FloatArray > target = ArrayImgs.floats( 1, f.length );

		DistanceTransform.transform( img, target, DISTANCE_TYPE.EUCLIDIAN, 1, 1.0 );

		for ( final FloatType i : img )
			System.out.print( i + ", " );
		System.out.println();

		for ( final FloatType i : target )
			System.out.print( i + ", " );
		System.out.println();

	}

}
