package edu.kit.trufflehog.view.graph.decorators;

import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;

public abstract class ShapeUtil {

    public static PathIterator concatPathIterators(Iterable<? extends PathIterator> pathIt) {
        return new PathIteratorConcatenation(pathIt);
    }

    public static PathIterator concatPathIterators(Iterable<? extends Shape> shapes,
                                                   final AffineTransform at) {
        Function<Shape,PathIterator> getPathIterator = new Function<Shape,PathIterator>() {
            @Override
            public PathIterator apply(Shape s) {
                return s.getPathIterator(at);
            }
        };

        return concatPathIterators(Iterables.transform(shapes, getPathIterator));
    }

    public static PathIterator concatPathIterators(Iterable<? extends Shape> shapes,
                                                   final AffineTransform at,
                                                   final double flatness) {
        Function<Shape,PathIterator> getPathIterator = new Function<Shape,PathIterator>() {
            @Override
            public PathIterator apply(Shape s) {
                return s.getPathIterator(at, flatness);
            }
        };

        return concatPathIterators(Iterables.transform(shapes, getPathIterator));
    }

}