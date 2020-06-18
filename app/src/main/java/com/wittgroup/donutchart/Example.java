package com.wittgroup.donutchart;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.wittgroupinc.donutchart.DonutChart;


public class Example extends AppCompatActivity {

    private DonutChart donutChart;

    private Button b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        b = findViewById(R.id.b);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                donutChart.animate_time();
            }
        });
        drawChart();
    }


    private void drawChart() {
        donutChart = findViewById(R.id.donutchart);
        donutChart.clear();
        donutChart.addSector(getResources().getColor(R.color.one),50);
        donutChart.addSector(getResources().getColor(R.color.two),20);
        donutChart.addSector(getResources().getColor(R.color.three),10);
        donutChart.addSector(getResources().getColor(R.color.four),10);
        donutChart.addSector(getResources().getColor(R.color.five),10);
    }
}
