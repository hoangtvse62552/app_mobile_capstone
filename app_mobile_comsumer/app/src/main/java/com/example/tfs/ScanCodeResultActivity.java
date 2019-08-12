package com.example.tfs;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

public class ScanCodeResultActivity extends AppCompatActivity {


    private int providerId;
    private int foodId;
    private int distributorId;
    private TextView farm;
    private TextView provider;
    private TextView txtProviderAddress;
    private TextView txtFarmerAddress;
    private TextView txtFoodId;
    private TextView txtCateId;
    private TextView txtBreed;
    private TextView txtCreateDate;
    private Provider providerDto;
    private Farm farmDto;
    private TextView txtVaccination;
    private TextView txtFeedings;
    private TextView txtTreatment;
    private TextView txtDistributor;
    private TextView txtDistributorAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_code_result);


        Intent it = this.getIntent();
        foodId = Integer.parseInt(it.getStringExtra("foodId"));
        providerId = Integer.parseInt(it.getStringExtra("providerId"));
        distributorId = Integer.parseInt(it.getStringExtra("distributorId"));

        init();
        getInformation();

    }

    public void init() {
        farm = findViewById(R.id.txtFarmer);
        txtFarmerAddress = findViewById(R.id.txtFarmerAddress);
        provider = findViewById(R.id.txtProvider);
        txtProviderAddress = findViewById(R.id.txtProviderAddress);
        txtBreed = findViewById(R.id.txtBreed);
        txtCateId = findViewById(R.id.txtCateId);
        txtCreateDate = findViewById(R.id.txtCreateDate);
        txtFoodId = findViewById(R.id.txtFoodId);
        txtFeedings = findViewById(R.id.txtFeedings);
        txtTreatment = findViewById(R.id.txtTreatment);
        txtVaccination = findViewById(R.id.txtVaccination);
        txtDistributor = findViewById(R.id.txtDistributor);
        txtDistributorAddress = findViewById(R.id.txtDistributorName);
    }

    public void getInformation() {
        FoodService.getInstance().getFoods(ScanCodeResultActivity.this, foodId, new VolleyCallBack() {
            @Override
            public void onSuccess(Object response) {
                try {
                    String json = response.toString();
                    ObjectMapper mapper = new ObjectMapper();
                    JsonNode jsonNode = mapper.readTree(json);


                    farmDto = new Farm();
                    providerDto = new Provider();

                    int farmId = jsonNode.get("Farm").get("FarmId").asInt();

                    txtCateId.setText(jsonNode.get("Category").asText());

                    txtCreateDate.setText(jsonNode.get("StartedDate").asText().substring(0, 10));
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
                                System.out.println(providerDto.getName() + "   and  " + providerDto.getAddress());

                                JsonNode treatment = objNode.get("Treatment");
                                if (!treatment.asText().equals("null")) {
                                    treatmentDate = objNode.get("Treatment").get("TreatmentDate").asText();
                                    treatmentProcess = new ArrayList<>();
                                    final JsonNode arrNodeTreatment = objNode.get("Treatment").get("TreatmentProcess");
                                    if (arrNodeTreatment.isArray()) {
                                        for (final JsonNode provider : arrNodeTreatment) {
                                            treatmentProcess.add(provider.toString());
                                        }
                                    }
                                }
                            }
                        }
                    }
                    Treatment treatment = new Treatment();
                    treatment.setTreatmentProcess(treatmentProcess);
                    treatment.setTreatmentDate(treatmentDate);
                    providerDto.setTreatment(treatment);

                    final JsonNode arrNodeDistributor = new ObjectMapper().readTree(json).get("Distributors");
                    if (arrNodeDistributor.isArray()) {
                        for (final JsonNode objNode : arrNodeDistributor) {
                            if (objNode.get("DistributorId").asInt() == distributorId) {
                                txtDistributor.setText(objNode.get("Name").asText());
                                txtDistributorAddress.setText(objNode.get("Address").asText());
                            }
                        }
                    }

                    String vaccin = "";
                    for (int i = 0; i < farmDto.getVaccinations().size(); i++) {
                        String vaccine = farmDto.getVaccinations().get(i).getVaccinationType();
                        vaccin += "+ Ngày tiêm: " + farmDto.getVaccinations().get(i).getVaccinationDate().substring(1, 11) + "\n   Tên: " + vaccine.substring(1, vaccine.length() - 1) + "\n";
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
                    if(providerDto.getTreatment().getTreatmentProcess() != null) {
                        for (int i = 0; i < providerDto.getTreatment().getTreatmentProcess().size(); i++) {
                            String s = providerDto.getTreatment().getTreatmentProcess().get(i);
                            treatmentProcesss += s.substring(1, s.length() - 1) + "\n";
                        }
                    }
                    if (treatmentProcesss.length() == 0) {
                        txtTreatment.setText("Không có thông tin");
                    } else {
                        txtTreatment.setText(treatmentProcesss);
                    }
                    provider.setText(providerDto.getName());
                    txtProviderAddress.setText(providerDto.getAddress());
                    txtFoodId.setText("TFS-" + foodId + "-" + farmId + "-" + providerId);

                } catch (Exception e) {

                }
            }

            @Override
            public void onError(Object ex) {
                System.out.println(ex);
                Toast.makeText(ScanCodeResultActivity.this, "Load information fail", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void goToHome(View view) {
        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }
}
