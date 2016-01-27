package edu.kit.trufflehog.presenter;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.management.InstanceAlreadyExistsException;

/**
 * <p>
 *      The Presenter builds TruffleHog. It instantiates all necessary instances of classes, registers these instances
 *      with each other, distributes resources (parameters) to them etc. In other words it is the glue code of
 *      TruffleHog. There should always only be one instance of a Presenter around.
 * </p>
 */
public class Presenter {

    /**
     * <p>
     *     Creates a new instance of a Presenter. A presenter is a singelton, thus InstanceAlreadyExistsException is
     *     thrown when an instance has already been created.
     * </p>
     *
     * @return A new instance of the Presenter, if none already exists.
     * @throws InstanceAlreadyExistsException Thrown if this method is called when an instance of the Presenter
     *         already exists.
     */
    public static Presenter createPresenter() throws InstanceAlreadyExistsException {
        throw new NotImplementedException();
    }

    /**
     * <p>
     *     Creates a new instance of a Presenter.
     * </p>
     */
    private Presenter() {
    }

    /**
     *  <p>
     *      Present TruffleHog. Create all necessary objects, register them with each other, bind them, pass them the
     *      resources they need ect.
     *  </p>
     */
    public void present() {

    }
}
