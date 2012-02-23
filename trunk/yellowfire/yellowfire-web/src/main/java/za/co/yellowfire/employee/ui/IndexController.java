package za.co.yellowfire.employee.ui;

import za.co.yellowfire.performance.ui.AbstractPerformanceUIController;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

/**
 * @author Mark P Ashworth
 * @version 0.0.1
 */
@RequestScoped @Named("employeeIndexController")
public class IndexController extends AbstractEmployeeUIController {
    private static final long serialVersionUID = 1L;
}
