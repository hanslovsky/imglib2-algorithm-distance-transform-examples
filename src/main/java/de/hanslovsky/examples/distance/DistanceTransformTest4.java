package de.hanslovsky.examples.distance;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ij.ImageJ;
import ij.ImagePlus;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.algorithm.morphology.distance.DistanceTransform;
import net.imglib2.algorithm.morphology.distance.DistanceTransform.DISTANCE_TYPE;
import net.imglib2.algorithm.morphology.distance.EuclidianDistanceAnisotropic;
import net.imglib2.converter.Converter;
import net.imglib2.converter.Converters;
import net.imglib2.converter.read.ConvertedRandomAccessibleInterval;
import net.imglib2.exception.IncompatibleTypeException;
import net.imglib2.img.array.ArrayImg;
import net.imglib2.img.array.ArrayImgs;
import net.imglib2.img.basictypeaccess.array.DoubleArray;
import net.imglib2.img.basictypeaccess.array.FloatArray;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.type.numeric.real.DoubleType;
import net.imglib2.type.numeric.real.FloatType;
import net.imglib2.util.Pair;
import net.imglib2.view.Views;

public class DistanceTransformTest4 {

	public static void main( final String[] args ) throws IncompatibleTypeException, InterruptedException, ExecutionException
	{

		new ImageJ();

//		final String url = "http://img.autobytel.com/car-reviews/autobytel/11694-good-looking-sports-cars/2016-Ford-Mustang-GT-burnout-red-tire-smoke.jpg";
		final String url = "/home/hanslovskyp/Dropbox/misc/butterfly.jpg";
//		final String url = "http://www.tomgibara.com/images/canny-example-edges.png";
		final ImagePlus imp = new ImagePlus( url );
//		imp.show();

		final ArrayImg< FloatType, FloatArray > img = ArrayImgs.floats( ( float[] ) imp.getProcessor().convertToFloatProcessor().getPixels(), imp.getWidth(), imp.getHeight() );

		final Converter< FloatType, DoubleType > conv = ( input, output ) -> {
			output.set( input.getRealDouble() );
			output.mul( -1.0 );
		};

		ImageJFunctions.show( Converters.convert( ( RandomAccessibleInterval< FloatType > ) img, conv, new DoubleType() ), "img" );

		final int nThreads = Runtime.getRuntime().availableProcessors();
		final ExecutorService es = Executors.newFixedThreadPool( nThreads );
		System.out.println( nThreads + " threads" );
		final ArrayImg< DoubleType, DoubleArray > dt1 = ArrayImgs.doubles( imp.getWidth(), imp.getHeight() );
		final ArrayImg< DoubleType, DoubleArray > dt2 = ArrayImgs.doubles( imp.getWidth(), imp.getHeight() );
		final ArrayImg< DoubleType, DoubleArray > tmp = ArrayImgs.doubles( imp.getWidth(), imp.getHeight() );

		final boolean doEuclidian = false;
		if ( doEuclidian )
		{
			DistanceTransform.transform(
					new ConvertedRandomAccessibleInterval<>( img, conv, new DoubleType() ),
					dt1,
					new EuclidianDistanceAnisotropic( 5e-1, 5e-1 ),
					es,
					3 * nThreads );
			DistanceTransform.transform(
					new ConvertedRandomAccessibleInterval<>( img, conv, new DoubleType() ),
					tmp,
					dt2,
					new EuclidianDistanceAnisotropic( 5e-1, 5e-1 ),
					es,
					3 * nThreads );
		}
		else
		{
			DistanceTransform.transform(
					new ConvertedRandomAccessibleInterval<>( img, conv, new DoubleType() ),
					dt1,
					DISTANCE_TYPE.L1,
					es,
					3 * nThreads,
					5e0 );
			DistanceTransform.transform(
					new ConvertedRandomAccessibleInterval<>( img, conv, new DoubleType() ),
					tmp,
					dt2,
					DISTANCE_TYPE.L1,
					es,
					3 * nThreads,
					5e0 );
		}
		for ( final Pair< DoubleType, DoubleType > p : Views.interval( Views.pair( dt1, dt2 ), dt1 ) )
		{
			if ( !p.getA().valueEquals( p.getB() ) )
			{
				System.out.println( "RESULTS ARE NOT THE SAME!" );
				throw new RuntimeException( "RESULTS ARE NOT THE SAME!" );
			}
		}
		ImageJFunctions.show( dt1, "dt is tmp" );
		ImageJFunctions.show( dt2, "additional tmp" );
		es.shutdown();
	}

}
