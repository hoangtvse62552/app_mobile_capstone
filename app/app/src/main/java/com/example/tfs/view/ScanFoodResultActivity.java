package com.example.tfs.view;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tfs.R;
import com.example.tfs.api.VolleyCallBack;
import com.example.tfs.dto.Farm;
import com.example.tfs.dto.Provider;
import com.example.tfs.dto.Transaction;
import com.example.tfs.dto.Treatment;
import com.example.tfs.dto.Vaccinations;
import com.example.tfs.service.FoodService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

public class ScanFoodResultActivity extends AppCompatActivity {

    private int foodId;
    private int distributorId;
    private TextView farm;
    private TextView provider;
    private TextView txtProviderAddress;
    private TextView txtFarmerAddress;
    private TextView tv;
    private TextView txtFoodId;
    private TextView txtCateId;
    private TextView txtBreed;
    private TextView txtCreateDate;
    private Dialog myDialog;
    private Button btnConfirm;
    private Button btnReject;
    private int status;
    private int userID;
    private Provider providerDto;
    private Farm farmDto;
    private TextView txtVaccination;
    private TextView txtFeedings;
    private TextView txtTreatment;
    private Process process;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_food_result);
//        innitView();


        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        distributorId = sharedPreferences.getInt("premisesId", 0);
        Intent it = this.getIntent();
        foodId = Integer.parseInt(it.getStringExtra("foodId"));
        int id = Integer.parseInt(it.getStringExtra("providerId"));
        loadFoodData(foodId, id);
    }

    private void innitView() {
        farm = findViewById(R.id.txtFarmer1);
        txtFarmerAddress = findViewById(R.id.txtFarmerAddress1);
        provider = findViewById(R.id.txtProvider1);
        txtProviderAddress = findViewById(R.id.txtProviderAddress1);
        tv = findViewById(R.id.txtResult);
        txtBreed = findViewById(R.id.txtBreed1);
        txtCateId = findViewById(R.id.txtCateId1);
        txtCreateDate = findViewById(R.id.txtCreateDate1);
        txtFoodId = findViewById(R.id.txtFoodId1);
        txtFeedings = findViewById(R.id.txtFeedings1);
        txtTreatment = findViewById(R.id.txtTreatment1);
        txtVaccination = findViewById(R.id.txtVaccination1);


    }

    public void loadFoodData(final int foodId, final int providerId) {
        FoodService.getInstance().getFoods(ScanFoodResultActivity.this, foodId, new VolleyCallBack() {
            @Override
            public void onSuccess(Object response) {
                try {
                    String json = response.toString();
                    ObjectMapper mapper = new ObjectMapper();
                    JsonNode jsonNode = mapper.readTree(json);

                    farm = findViewById(R.id.txtFarmer1);
                    txtFarmerAddress = findViewById(R.id.txtFarmerAddress1);
                    provider = findViewById(R.id.txtProvider1);
                    txtProviderAddress = findViewById(R.id.txtProviderAddress1);
                    txtBreed = findViewById(R.id.txtBreed1);
                    txtCateId = findViewById(R.id.txtCateId1);
                    txtCreateDate = findViewById(R.id.txtCreateDate1);
                    txtFoodId = findViewById(R.id.txtFoodId1);
                    txtFeedings = findViewById(R.id.txtFeedings1);
                    txtTreatment = findViewById(R.id.txtTreatment1);
                    txtVaccination = findViewById(R.id.txtVaccination1);
                    providerDto = new Provider();
                    farmDto = new Farm();



                    txtCateId.setText(jsonNode.get("Category").asText());
                    System.out.println("aaaaaaaaaaaaaa " + jsonNode.get("Breed").asText());
                    txtFoodId.setText("" + foodId);
                    txtCreateDate.setText(jsonNode.get("StartedDate").asText());
                    txtBreed.setText(jsonNode.get("Breed").asText());
                    farm.setText(jsonNode.get("Farm").get("Name").asText());
                    txtFarmerAddress.setText(jsonNode.get("Farm").get("Address").asText());

                    //get Feedings
                    List<String> feed = new ArrayList<>();
                    final JsonNode arrNodeFeeding = new ObjectMapper().readTree(json).get("Farm").get("Feedings");
                    if (arrNodeFeeding.isArray()) {
                        for (final JsonNode objNode : arrNodeFeeding) {
                            feed.add(objNode.toString());
                        }
                    }
                    farmDto.setFeedings(feed);


//                    //get Vaccinations
                    List<Vaccinations> listVaccin = new ArrayList<>();
                    final JsonNode arrNode = new ObjectMapper().readTree(json).get("Farm").get("Vaccinations");
                    if (arrNode.isArray()) {
                        for (final JsonNode objNode : arrNode) {
                            Vaccinations dto = new Vaccinations();
                            dto.setVaccinationDate(objNode.get("VaccinationDate").toString());
                            dto.setVaccinationType(objNode.get("VaccinationType").toString());
                            listVaccin.add(dto);
                        }
                    }
                    farmDto.setVaccinations(listVaccin);

                    String treatmentDate = "";
                    List<String> treatmentProcess = null;
                    final JsonNode arrNodeProvider = new ObjectMapper().readTree(json).get("Providers");
                    if (arrNodeProvider.isArray()) {
                        for (final JsonNode objNode : arrNodeProvider) {
                            if (objNode.get("ProviderId").asInt() == providerId) {
                                providerDto.setName(objNode.get("Name").asText());
                                providerDto.setAddress(objNode.get("Address").asText());
                                treatmentDate = objNode.get("Treatment").get("TreatmentDate").asText();
                                treatmentProcess = new ArrayList<>();
                                final JsonNode arrNodeTreatment = objNode.get("Treatment").get("TreatmentProcess");
                                if (arrNodeTreatment.isArray()) {
                                    for (final JsonNode provider : arrNodeTreatment) {
                                        treatmentProcess.add(provider.toString());
                                        System.out.println(provider.toString());
                                    }
                                }
                            }
                        }
                    }


                    Treatment treatment = new Treatment();
                    treatment.setTreatmentProcess(treatmentProcess);
                    treatment.setTreatmentDate(treatmentDate);
                    providerDto.setTreatment(treatment);

                    String vaccin = "";
                    for (int i = 0; i < farmDto.getVaccinations().size(); i++) {
                        vaccin += "+ " + farmDto.getVaccinations().get(i).getVaccinationDate().substring(1, 11) + "\n" + farmDto.getVaccinations().get(i).getVaccinationType() + "\n";
                    }
                    if (vaccin.length() == 0) {
                        txtVaccination.setText("Không có thông tin");
                    } else {
                        txtVaccination.setText(vaccin);
                    }


                    String feeding = "";
                    for (int i = 0; i < farmDto.getFeedings().size(); i++) {
                        feeding += "+ " + farmDto.getFeedings().get(i) + "\n";
                    }
                    if (feeding.length() == 0) {
                        txtFeedings.setText("Không có thông tin");
                    } else {
                        txtFeedings.setText(feeding);
                    }

//
                    String treatmentProcesss = "";
                    for (int i = 0; i < providerDto.getTreatment().getTreatmentProcess().size(); i++) {
                        treatmentProcesss += providerDto.getTreatment().getTreatmentProcess().get(i);
                    }
                    if (treatmentProcesss.length() == 0) {
                        txtTreatment.setText("Không có thông tin");
                    } else {
                        txtTreatment.setText(treatmentProcesss);
                    }



                    provider.setText(providerDto.getName());
                    txtProviderAddress.setText(providerDto.getAddress());

                } catch (Exception e) {

                }
            }

            @Override
            public void onError(Object ex) {

            }
        });
    }

    public void onClickConfirmButton(View v) {
        FoodService.getInstance().updateDistributorFood(ScanFoodResultActivity.this, distributorId, foodId, new VolleyCallBack() {
            @Override
            public void onSuccess(Object response) {
                success();
            }

            @Override
            public void onError(Object ex) {
                fail();
            }
        });
    }

    public void onClickRejectButton(View v) {
        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
    }

    public void success() {
        Toast.makeText(ScanFoodResultActivity.this, "Xác nhận thành công", Toast.LENGTH_LONG);
        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
    }

    public void fail() {
        Toast.makeText(ScanFoodResultActivity.this, "Xác nhận thất bại", Toast.LENGTH_LONG);
    }
}
