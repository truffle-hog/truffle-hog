package edu.kit.trufflehog.view.graph.decorators;

import java.awt.geom.PathIterator;
import java.util.Arrays;
import java.util.Iterator;

final class PathIteratorConcatenation implements PathIterator {

    private final Iterator<? extends PathIterator> pathIts;
    private PathIterator current = null;

    private void advance() {
        while(current == null || current.isDone()) {
            if(!pathIts.hasNext()) {
                current = null;
                break;
            }
            current = pathIts.next();
        }
    }

    public PathIteratorConcatenation(PathIterator ...pathIts) {
        this(Arrays.asList(pathIts));
    }

    public PathIteratorConcatenation(Iterator<? extends PathIterator> pathIts) {
        this.pathIts = pathIts;
        advance();
    }

    public PathIteratorConcatenation(Iterable<? extends PathIterator> pathIts) {
        this(pathIts.iterator());
    }

    @Override
    public int getWindingRule() {

        if (current == null) {
            return 0;
        }

        return current.getWindingRule();
    }

    @Override
    public boolean isDone() {
        return (current == null);
    }

    @Override
    public void next() {
        current.next();
        advance();
    }

    @Override
    public int currentSegment(float[] coords) {
        return current.currentSegment(coords);
    }

    @Override
    public int currentSegment(double[] coords) {
        return current.currentSegment(coords);
    }


}
