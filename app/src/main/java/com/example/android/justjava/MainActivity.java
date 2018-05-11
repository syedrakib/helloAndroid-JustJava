/**
 * IMPORTANT: Make sure you are using the correct package name.
 * This example uses the package name:
 * package com.example.android.justjava
 * If you get an error when copying this code into Android studio, update it to match teh package name found
 * in the project's AndroidManifest.xml file.
 **/

package com.example.android.justjava;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;

/**
 * This app displays an order form to order coffee.
 */
public class MainActivity extends AppCompatActivity {

    int numberOfCoffee = 0;
    boolean addChocolate = false;
    boolean addStrawberry = false;
    boolean addWhippedCream = false;
    final int coffeeRate = 5;
    final int chocolateRate = 1;
    final int strawberryRate = 2;
    final int whippedCreamRate = 3;
    int totalPrice = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        displayQuantity();
        displayTotalPrice();
        disappearOrderSummary();
    }

    /**
     * This method is called when the quantity counter us incremented.
     */
    public void incrementQuantity(View view) {
        numberOfCoffee++;
        displayQuantity();
        calculateTotalPrice();
    }

    /**
     * This method is called when the quantity counter us decremented.
     */
    public void decrementQuantity(View view) {
        if (numberOfCoffee > 0){
            numberOfCoffee--;
            displayQuantity();
            calculateTotalPrice();
        }
    }

    /**
     * This method displays the given quantity value on the screen.
     */
    private void displayQuantity() {
        TextView quantityTextView = (TextView) findViewById(R.id.quantity_text_view);
        quantityTextView.setText("" + numberOfCoffee);
    }

    /**
     * This method sets user preference to add chocolate topping or not
     */
    public void setChocolateTopping(View view){
        CheckBox chocolateToppingCheckbox = (CheckBox) findViewById(R.id.chocolate_topping_checkbox);
        addChocolate = chocolateToppingCheckbox.isChecked();
        calculateTotalPrice();
    }

    /**
     * This method sets user preference to add strawberry topping or not
     */
    public void setStrawberryTopping(View view){
        CheckBox strawberryToppingCheckbox = (CheckBox) findViewById(R.id.strawberry_topping_checkbox);
        addStrawberry = strawberryToppingCheckbox.isChecked();
        calculateTotalPrice();
    }

    /**
     * This method sets user preference to add whipped cream topping or not
     */
    public void setWhippedCreamTopping(View view){
        CheckBox whippedCreamToppingCheckbox = (CheckBox) findViewById(R.id.whipped_cream_topping_checkbox);
        addWhippedCream = whippedCreamToppingCheckbox.isChecked();
        calculateTotalPrice();
    }

    /**
     * This method calculates total price of order.
     */
    private void calculateTotalPrice() {
        int pricePerCoffee = coffeeRate;
        if (addChocolate)
            pricePerCoffee += chocolateRate;
        if (addStrawberry)
            pricePerCoffee += strawberryRate;
        if (addWhippedCream)
            pricePerCoffee += whippedCreamRate;
        totalPrice = pricePerCoffee * numberOfCoffee;
        displayTotalPrice();
    }

    /**
     * This method displays the calculated total price on the screen.
     */
    private void displayTotalPrice() {
        TextView priceTextView = (TextView) findViewById(R.id.price_text_view);
        priceTextView.setText("" + getFormattedPrice(totalPrice));
    }

    /**
     * This method returns the given price value formatted with a currency symbol.
     */
    private String getFormattedPrice(int totalPrice) {
        return NumberFormat.getCurrencyInstance().format(totalPrice);
    }

    /**
     * This method is called when the order button is clicked.
     */
    public void submitOrder(View view) {
        if (numberOfCoffee > 0) {
            displayOrderSummary();
            scrollToOrderConfirmationButton();
        }
        else{
            disappearOrderSummary();
            Toast.makeText(
                    this, "Cannot order 0 item", Toast.LENGTH_SHORT
            ).show();
        }
    }

    /**
     * This method scrolls the page to the order confirmation butotn
     */
    private void scrollToOrderConfirmationButton(){
        Button confirmationButton = (Button) findViewById(R.id.confirmation_button);
        ScrollView parentScrollView = (ScrollView) findViewById(R.id.parent_scrollview);
        parentScrollView.smoothScrollTo(0, (int) confirmationButton.getY());
    }

    /**
     * This method displays the full order summary section.
     */
    private void displayOrderSummary() {
        View orderSummarySection = findViewById(R.id.order_summary_section);
        orderSummarySection.setVisibility(View.VISIBLE);
        TextView orderSummaryTextView = (TextView) findViewById(R.id.order_summary_text_view);
        orderSummaryTextView.setText(createOrderSummary());
    }

    /**
     * This method creates the order summary text to be displayed
     */
    private String createOrderSummary(){
        EditText customerNameField = (EditText) findViewById(R.id.customer_name_field);
        String customerName = customerNameField.getText().toString();
        String orderSummary = getString(
            R.string.order_summary_contents,
            customerName,
            numberOfCoffee,
            addChocolate, addStrawberry, addWhippedCream,
            getFormattedPrice(totalPrice)
        );
        return orderSummary;
    }

    /**
     * This method hides the order summary section.
     */
    private void disappearOrderSummary(){
        View orderSummarySection = findViewById(R.id.order_summary_section);
        orderSummarySection.setVisibility(View.INVISIBLE);
    }

    /**
     * This method is called when user finally confirms his/her order
     */
    public void confirmOrder(View view){
        TextView orderSummaryTextView = (TextView) findViewById(R.id.order_summary_text_view);
        sendEmail(orderSummaryTextView.getText().toString());
        disappearOrderSummary();
    }

    /**
     * This method simply sends an email to place the order
     */
    private void sendEmail(String emailContents){
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"rakib@example.com"});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Get me Coffee");
        emailIntent.putExtra(Intent.EXTRA_TEXT, emailContents);
        if (emailIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(emailIntent);
        }
    }

}