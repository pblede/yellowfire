package za.co.yellowfire.performance.ui;

import za.co.yellowfire.ui.training.AbstractTrainingUIController;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

/**
 * @author Mark P Ashworth
 * @version 0.0.1
 */
@RequestScoped @Named("performanceIndexController")
public class IndexController extends AbstractPerformanceUIController {
    private static final long serialVersionUID = 1L;
}
