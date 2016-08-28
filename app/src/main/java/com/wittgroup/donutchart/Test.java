package com.wittgroup.donutchart;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.wittgroup.donutchart.donutchart.ChartData;
import com.wittgroup.donutchart.donutchart.DonutChart;


public class Test extends AppCompatActivity {

    private DonutChart donutChart;

    private Button b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        b = (Button) findViewById(R.id.b);
        drawChart();

    }


    private void drawChart() {
        donutChart = (DonutChart) findViewById(R.id.donutchart);
        donutChart.clear();
        int color = getResources().getColor(R.color.skill_match_compatibility);
        ChartData chartData = new ChartData(color);
        chartData.setSectorValue((float) 50);
        donutChart.addSector(chartData);
        color = getResources().getColor(R.color.experience_compatibility);
        chartData = new ChartData(color);
        chartData.setSectorValue((float) 20);
        donutChart.addSector(chartData);
        color = getResources().getColor(R.color.location_compatibility);
        chartData = new ChartData(color);
        chartData.setSectorValue((float) 10);
        donutChart.addSector(chartData);
        color = getResources().getColor(R.color.project_worked_compatibility);
        chartData = new ChartData(color);
        chartData.setSectorValue((float) 10);
        donutChart.addSector(chartData);
        color = getResources().getColor(R.color.rating_compatibility);
        chartData = new ChartData(color);
        chartData.setSectorValue((float) 10);
        donutChart.addSector(chartData);
        donutChart.commit();


    }
}
