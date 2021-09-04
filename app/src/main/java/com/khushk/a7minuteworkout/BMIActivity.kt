package com.khushk.a7minuteworkout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.RadioGroup
import android.widget.Toast
import com.khushk.a7minuteworkout.databinding.ActivityBmiactivityBinding
import java.math.BigDecimal
import java.math.RoundingMode

class BMIActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBmiactivityBinding

    val MERTIC_UNITS_VIEW = "METRIC_UNIT_VIEW"
    val US_UNITS_VIEW = "US_UNIT_VIEW"

    var currentVisibilityView: String = MERTIC_UNITS_VIEW

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBmiactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarBmiActivity)


        supportActionBar?.setDisplayHomeAsUpEnabled(true) //set back button
        supportActionBar?.title = "CALCULATE BMI" // Setting a title in the action bar.

        binding.toolbarBmiActivity.setNavigationOnClickListener {
            onBackPressed()
        }

        makeVisibleMetricUnitsView()

        // Radio Group change listener is set to the radio group which is added in XML.
       binding.rgUnits.setOnCheckedChangeListener { radioGroup: RadioGroup, checkedId: Int ->

            // Here is the checkId is METRIC UNITS view then make the view visible else US UNITS view.
            if (checkedId == R.id.rbMetricUnits) {
                makeVisibleMetricUnitsView()
            } else {
                makeVisibleUsUnitsView()
            }
        }

        // Button will calculate the input values in Metric Units
        binding.btnCalculateUnits.setOnClickListener {

            if (currentVisibilityView.equals(MERTIC_UNITS_VIEW)) {
                // The values are validated.
                if (validateMetricUnits()) {

                    // The height value in converted to float value and divided by 100 to convert it to meter.
                    val heightValue: Float =  binding.etMetricUnitHeight.text.toString().toFloat() / 100

                    // The weight value in converted to float value
                    val weightValue: Float =  binding.etMetricUnitWeight.text.toString().toFloat()

                    // BMI value is calculated in METRIC UNITS using the height and weight value.
                    val bmi = weightValue / (heightValue * heightValue)

                    displayBMIResult(bmi)
                } else {
                    Toast.makeText(
                        this@BMIActivity,
                        "Please enter valid values.",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            } else {

                // The values are validated.
                if (validateUsUnits()) {

                    val usUnitHeightValueFeet: String =
                        binding.etUsUnitHeightFeet.text.toString() // Height Feet value entered in EditText component.
                    val usUnitHeightValueInch: String =
                        binding.etUsUnitHeightInch.text.toString() // Height Inch value entered in EditText component.
                    val usUnitWeightValue: Float =  binding.etUsUnitWeight.text.toString()
                        .toFloat() // Weight value entered in EditText component.

                    // Here the Height Feet and Inch values are merged and multiplied by 12 for converting it to inches.
                    val heightValue =
                        usUnitHeightValueInch.toFloat() + usUnitHeightValueFeet.toFloat() * 12

                    // This is the Formula for US UNITS result.
                    // Reference Link : https://www.cdc.gov/healthyweight/assessing/bmi/childrens_bmi/childrens_bmi_formula.html
                    val bmi = 703 * (usUnitWeightValue / (heightValue * heightValue))

                    displayBMIResult(bmi) // Displaying the result into UI
                } else {
                    Toast.makeText(
                        this@BMIActivity,
                        "Please enter valid values.",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            }
        }
    }

    /**
     * Function is used to make visible the METRIC UNITS VIEW and hide the US UNITS VIEW.
     */
    private fun makeVisibleMetricUnitsView() {
        currentVisibilityView = MERTIC_UNITS_VIEW // Current View is updated here.
        binding.llMetricUnitsView.visibility = View.VISIBLE // METRIC UNITS VIEW is Visible
        binding. llUsUnitsView.visibility = View.GONE // US UNITS VIEW is hidden

        binding.etMetricUnitHeight.text!!.clear() // height value is cleared if it is added.
        binding. etMetricUnitWeight.text!!.clear() // weight value is cleared if it is added.

        binding. tvYourBMI.visibility = View.INVISIBLE // Result is cleared and the labels are hidden
        binding.tvBMIValue.visibility = View.INVISIBLE // Result is cleared and the labels are hidden
        binding. tvBMIType.visibility = View.INVISIBLE // Result is cleared and the labels are hidden
        binding. tvBMIDescription.visibility = View.INVISIBLE // Result is cleared and the labels are hidden
    }

    /**
     * Function is used to make visible the US UNITS VIEW and hide the METRIC UNITS VIEW.
     */
    private fun makeVisibleUsUnitsView() {
        currentVisibilityView = US_UNITS_VIEW // Current View is updated here.
        binding. llMetricUnitsView.visibility = View.GONE // METRIC UNITS VIEW is hidden
        binding. llUsUnitsView.visibility = View.VISIBLE // US UNITS VIEW is Visible

        binding. etUsUnitWeight.text!!.clear() // weight value is cleared if it is added.
        binding. etUsUnitHeightFeet.text!!.clear() // height feet value is cleared if it is added.
        binding. etUsUnitHeightInch.text!!.clear() // height inch is cleared if it is added.

        binding. tvYourBMI.visibility = View.INVISIBLE // Result is cleared and the labels are hidden
        binding.tvBMIValue.visibility = View.INVISIBLE // Result is cleared and the labels are hidden
        binding.tvBMIType.visibility = View.INVISIBLE // Result is cleared and the labels are hidden
        binding.tvBMIDescription.visibility = View.INVISIBLE // Result is cleared and the labels are hidden
    }
    // END



    /**
     * Function is used to display the result of METRIC UNITS.
     */
    private fun displayBMIResult(bmi: Float) {

        val bmiLabel: String
        val bmiDescription: String

        if (java.lang.Float.compare(bmi, 15f) <= 0) {
            bmiLabel = "Very severely underweight"
            bmiDescription = "Oops! You really need to take care of your better! Eat more!"
        } else if (java.lang.Float.compare(bmi, 15f) > 0 && java.lang.Float.compare(
                bmi,
                16f
            ) <= 0
        ) {
            bmiLabel = "Severely underweight"
            bmiDescription = "Oops! You really need to take care of your better! Eat more!"
        } else if (java.lang.Float.compare(bmi, 16f) > 0 && java.lang.Float.compare(
                bmi,
                18.5f
            ) <= 0
        ) {
            bmiLabel = "Underweight"
            bmiDescription = "Oops! You really need to take care of your better! Eat more!"
        } else if (java.lang.Float.compare(bmi, 18.5f) > 0 && java.lang.Float.compare(
                bmi,
                25f
            ) <= 0
        ) {
            bmiLabel = "Normal"
            bmiDescription = "Congratulations! You are in a good shape!"
        } else if (java.lang.Float.compare(bmi, 25f) > 0 && java.lang.Float.compare(
                bmi,
                30f
            ) <= 0
        ) {
            bmiLabel = "Overweight"
            bmiDescription = "Oops! You really need to take care of your yourself! Workout maybe!"
        } else if (java.lang.Float.compare(bmi, 30f) > 0 && java.lang.Float.compare(
                bmi,
                35f
            ) <= 0
        ) {
            bmiLabel = "Obese Class | (Moderately obese)"
            bmiDescription = "Oops! You really need to take care of your yourself! Workout maybe!"
        } else if (java.lang.Float.compare(bmi, 35f) > 0 && java.lang.Float.compare(
                bmi,
                40f
            ) <= 0
        ) {
            bmiLabel = "Obese Class || (Severely obese)"
            bmiDescription = "OMG! You are in a very dangerous condition! Act now!"
        } else {
            bmiLabel = "Obese Class ||| (Very Severely obese)"
            bmiDescription = "OMG! You are in a very dangerous condition! Act now!"
        }

        binding. tvYourBMI.visibility = View.VISIBLE
        binding. tvBMIValue.visibility = View.VISIBLE
        binding. tvBMIType.visibility = View.VISIBLE
        binding. tvBMIDescription.visibility = View.VISIBLE

        // This is used to round of the result value to 2 decimal values after "."
        val bmiValue = BigDecimal(bmi.toDouble()).setScale(2, RoundingMode.HALF_EVEN).toString()

        binding.tvBMIValue.text = bmiValue // Value is set to TextView
        binding.tvBMIType.text = bmiLabel // Label is set to TextView
        binding. tvBMIDescription.text = bmiDescription // Description is set to TextView
    }
    /**
     * Function is used to validate the input values for METRIC UNITS.
     */
    private fun validateMetricUnits(): Boolean {
        var isValid = true

        if ( binding.etMetricUnitWeight.text.toString().isEmpty()) {
            isValid = false
        } else if ( binding.etMetricUnitHeight.text.toString().isEmpty()) {
            isValid = false
        }

        return isValid
    }

    /**
     * Function is used to validate the input values for US UNITS.
     */
    private fun validateUsUnits(): Boolean {
        var isValid = true

        if ( binding.etUsUnitWeight.text.toString().isEmpty()) {
            isValid = false
        } else if ( binding.etUsUnitHeightFeet.text.toString().isEmpty()) {
            isValid = false
        } else if ( binding.etUsUnitHeightInch.text.toString().isEmpty()) {
            isValid = false
        }

        return isValid
    }
}