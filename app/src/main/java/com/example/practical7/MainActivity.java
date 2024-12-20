package com.example.practical7;import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;


public class MainActivity extends AppCompatActivity {

    private Button issueDateButton;
    private Button returnDateButton;
    private TextView issueDateTextView;
    private TextView returnDateTextView;
    private Button calculateButton;
    private TextView overdueTextView;
    private Date issueDate;
    private Date returnDate;
    private static final int MAX_OVERDUE_DAYS = 14;
    private static final int CHARGE_PER_DAY = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        issueDateButton = findViewById(R.id.issueDateButton);
        returnDateButton = findViewById(R.id.returnDateButton);
        issueDateTextView = findViewById(R.id.issueDateTextView);
        returnDateTextView = findViewById(R.id.returnDateTextView);
        calculateButton = findViewById(R.id.calculateButton);
        overdueTextView = findViewById(R.id.overdueTextView);

        issueDateButton.setOnClickListener(view -> showDatePickerDialog(true));
        returnDateButton.setOnClickListener(view -> showDatePickerDialog(false));
        calculateButton.setOnClickListener(view -> calculateOverdue());
    }
    private void showDatePickerDialog(final boolean isIssueDate) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    Calendar selectedCalendar = Calendar.getInstance();
                    selectedCalendar.set(selectedYear,selectedMonth,selectedDay);
                    Date selectedDate = selectedCalendar.getTime();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                    String formattedDate = dateFormat.format(selectedDate);

                    if(isIssueDate){
                        issueDate = selectedDate;
                        issueDateTextView.setText(formattedDate);
                    }
                    else {
                        returnDate = selectedDate;
                        returnDateTextView.setText(formattedDate);
                    }
                },year,month,day);
        datePickerDialog.show();
    }

    private void calculateOverdue() {


        if (issueDate == null || returnDate == null) {
            overdueTextView.setText("Please Select Both Date");
            return;
        }
        if(returnDate.before(issueDate)){
            overdueTextView.setText("Return date must be after issue date");
            return;

        }


        long timeDiff = returnDate.getTime() - issueDate.getTime();
        long daysDiff = TimeUnit.DAYS.convert(timeDiff, TimeUnit.MILLISECONDS);

        long overdueDays = Math.max(0, daysDiff - MAX_OVERDUE_DAYS);
        int charge = (int) (overdueDays * CHARGE_PER_DAY);

        String resultText = "Overdue: " + overdueDays + " days , Charge: " + charge + " Rs";

        if (overdueDays==0){
            resultText="No Overdue";
        }



        overdueTextView.setText(resultText);

    }
}