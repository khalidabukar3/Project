import components.naturalnumber.NaturalNumber;
import components.naturalnumber.NaturalNumber2;

/**
 * Controller class.
 *
 * @author Khalid Abukar
 */
public final class NNCalcController1 implements NNCalcController {

    /**
     * Model object.
     */
    private final NNCalcModel model;

    /**
     * View object.
     */
    private final NNCalcView view;

    /**
     * Useful constants.
     */
    private static final NaturalNumber TWO = new NaturalNumber2(2),
            INT_LIMIT = new NaturalNumber2(Integer.MAX_VALUE);

    /**
     * Updates this.view to display this.model, and to allow only operations
     * that are legal given this.model.
     *
     * @param model
     *            the model
     * @param view
     *            the view
     * @ensures [view has been updated to be consistent with model]
     */
    private static void updateViewToMatchModel(NNCalcModel model,
            NNCalcView view) {

        // Retrieve the top and bottom values from the model.
        NaturalNumber topValue = model.top();
        NaturalNumber bottomValue = model.bottom();

        // Update the view to display the top and bottom values.
        view.updateTopDisplay(topValue);
        view.updateBottomDisplay(bottomValue);

        /*
         * Enable or disable operations (subtract, divide, power, root) in view
         * based on model values.
         */

        view.updateSubtractAllowed(bottomValue.compareTo(topValue) <= 0);
        view.updateDivideAllowed(!bottomValue.isZero());
        view.updatePowerAllowed(bottomValue.compareTo(INT_LIMIT) <= 0);

        view.updateRootAllowed(bottomValue.compareTo(TWO) >= 0
                && bottomValue.compareTo(INT_LIMIT) <= 0);

    }

    /**
     * Constructor.
     *
     * @param model
     *            model to connect to
     * @param view
     *            view to connect to
     */
    public NNCalcController1(NNCalcModel model, NNCalcView view) {
        this.model = model;
        this.view = view;
        updateViewToMatchModel(model, view);
    }

    @Override
    public void processClearEvent() {
        /*
         * Get alias to bottom from model
         */
        NaturalNumber bottom = this.model.bottom();
        /*
         * Update model in response to this event
         */
        bottom.clear();
        /*
         * Update view to reflect changes in model
         */
        updateViewToMatchModel(this.model, this.view);
    }

    @Override
    public void processSwapEvent() {
        /*
         * Get aliases to top and bottom from model
         */
        NaturalNumber top = this.model.top();
        NaturalNumber bottom = this.model.bottom();
        /*
         * Update model in response to this event
         */
        NaturalNumber temp = top.newInstance();
        temp.transferFrom(top);
        top.transferFrom(bottom);
        bottom.transferFrom(temp);
        /*
         * Update view to reflect changes in model
         */
        updateViewToMatchModel(this.model, this.view);
    }

    @Override
    public void processEnterEvent() {

        // Retrieve the top and bottom values from the model.
        NaturalNumber topValue = this.model.top();
        NaturalNumber bottomValue = this.model.bottom();

        /*
         * Copies the bottom value to the top value in the model, then updates
         * the view.
         */

        topValue.copyFrom(bottomValue);

        updateViewToMatchModel(this.model, this.view);

    }

    @Override
    public void processAddEvent() {

        // Retrieve the top and bottom values from the model.
        NaturalNumber topValue = this.model.top();
        NaturalNumber bottomValue = this.model.bottom();

        /*
         * Adds the top value to the bottom value, clears the top value, then
         * updates the view.
         */

        bottomValue.add(topValue);
        topValue.clear();

        updateViewToMatchModel(this.model, this.view);

    }

    @Override
    public void processSubtractEvent() {

        // Retrieve the top and bottom values from the model.
        NaturalNumber topValue = this.model.top();
        NaturalNumber bottomValue = this.model.bottom();

        /*
         * Subtracts the bottom value from the top value, transfers the result
         * to bottom, then updates the view.
         */

        topValue.subtract(bottomValue);
        bottomValue.transferFrom(topValue);

        updateViewToMatchModel(this.model, this.view);

    }

    @Override
    public void processMultiplyEvent() {

        // Retrieve the top and bottom values from the model.
        NaturalNumber topValue = this.model.top();
        NaturalNumber bottomValue = this.model.bottom();

        /*
         * Multiplies the bottom value by the top value, clears the top value,
         * then updates the view.
         */

        bottomValue.multiply(topValue);
        topValue.clear();

        updateViewToMatchModel(this.model, this.view);

    }

    @Override
    public void processDivideEvent() {

        // Retrieve the top and bottom values from the model.
        NaturalNumber topValue = this.model.top();
        NaturalNumber bottomValue = this.model.bottom();

        /*
         * Divides the top value by the bottom value, stores the remainder in
         * top, then updates the view.
         */

        NaturalNumber divRemainder = topValue.divide(bottomValue);
        bottomValue.transferFrom(topValue);
        topValue.transferFrom(divRemainder);

        updateViewToMatchModel(this.model, this.view);

    }

    @Override
    public void processPowerEvent() {

        // Retrieve the top and bottom values from the model.
        NaturalNumber topValue = this.model.top();
        NaturalNumber bottomValue = this.model.bottom();

        /*
         * Raises the top value to the power of the bottom value's integer
         * representation, then updates the view.
         */

        topValue.power(bottomValue.toInt());
        bottomValue.transferFrom(topValue);

        updateViewToMatchModel(this.model, this.view);

    }

    @Override
    public void processRootEvent() {

        // Retrieve the top and bottom values from the model.
        NaturalNumber topValue = this.model.top();
        NaturalNumber bottomValue = this.model.bottom();

        /*
         * Takes the root of the top value based on the bottom value's integer
         * representation, then updates the view.
         */

        topValue.root(bottomValue.toInt());
        bottomValue.transferFrom(topValue);

        updateViewToMatchModel(this.model, this.view);

    }

    @Override
    public void processAddNewDigitEvent(int digit) {

        // Retrieve the bottom values from the model.
        NaturalNumber bottomValue = this.model.bottom();

        /*
         * Adds a new digit to the bottom value by multiplying by 10 and adding
         * the digit, then updates the view.
         */

        bottomValue.multiplyBy10(digit);

        updateViewToMatchModel(this.model, this.view);

    }

}
