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
import com.example.tfs.api.CONST;
import com.example.tfs.api.VolleyCallBack;
import com.example.tfs.dto.Farm;
import com.example.tfs.dto.Foods;
import com.example.tfs.dto.Provider;
import com.example.tfs.dto.Transaction;
import com.example.tfs.dto.Treatment;
import com.example.tfs.dto.Vaccinations;
import com.example.tfs.service.FoodService;
import com.example.tfs.service.TransactionService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ScanResultActivity extends AppCompatActivity {

    private TextView farm;
    private TextView provider;
    private TextView txtProviderAddress;
    private TextView txtFarmerAddress;
    private TextView tv;
    private Transaction transaction;
    private int transactionId;
    private int providerId;
    private EditText txtReason;
    private String reason;
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
    private String role;
    private String token;
    private int foodId;
    private int transactionStatus;
    private Button btnBack;
    private int premisesId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_result);
        innitView();
        myDialog = new Dialog(this);


        Intent it = this.getIntent();
        transactionId = Integer.parseInt(it.getStringExtra("transactionId"));
        providerId = Integer.parseInt(it.getStringExtra("providerId"));
        providerDto = new Provider();

        System.out.println(providerId + " providerID");
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        userID = sharedPreferences.getInt("userID", 0);
        token = sharedPreferences.getString("token", "");
        premisesId = sharedPreferences.getInt("premisesId", 0);
        if (token.contains("Veterinary")) {
            role = CONST.ROLE_VETERINARY;
        } else if (token.contains("Distributor")) {
            role = CONST.ROLE_DISTRIBUTOR;
        }

        loadInformation();
    }

    private void innitView() {
        farm = findViewById(R.id.txtFarmer);
        txtFarmerAddress = findViewById(R.id.txtFarmerAddress);
        provider = findViewById(R.id.txtProvider);
        txtProviderAddress = findViewById(R.id.txtProviderAddress);
        tv = findViewById(R.id.txtResult);
        txtBreed = findViewById(R.id.txtBreed);
        txtCateId = findViewById(R.id.txtCateId);
        txtCreateDate = findViewById(R.id.txtCreateDate);
        txtFoodId = findViewById(R.id.txtFoodId);
        txtFeedings = findViewById(R.id.txtFeedings);
        txtTreatment = findViewById(R.id.txtTreatment);
        txtVaccination = findViewById(R.id.txtVaccination);
    }

    public void onClickRejectButton(View v) {
        status = 4;
        showConfirmDialog();
    }

    public void onClickConfirmButton(View v) {
        if (role.equals("Veterinary")) {
            status = 2;
        } else {
            status = 3;
        }

        showConfirmDialog();
    }

    public void loadInformation() {

        tv = findViewById(R.id.txtResult);

        TransactionService.getInstance().getTransaction(ScanResultActivity.this, transactionId, role, new VolleyCallBack() {
            @Override
            public void onSuccess(Object response) {
                try {
                    String json = response.toString();
                    ObjectMapper mapper = new ObjectMapper();
                    JsonNode jsonNode = mapper.readTree(json);
                    foodId = jsonNode.get("data").get("FoodId").asInt();
                    transactionStatus = jsonNode.get("data").get("StatusId").asInt();
                    if (transactionStatus == 4 || (transactionStatus == 2 && role.equals(CONST.ROLE_VETERINARY))
                            || (transactionStatus != 2 && role.equals(CONST.ROLE_DISTRIBUTOR))) {
                        btnBack = findViewById(R.id.btnBack);
                        btnConfirm = findViewById(R.id.btnConfirm);
                        btnReject = findViewById(R.id.btnReject);

                        btnBack.setVisibility(View.VISIBLE);
                        btnConfirm.setVisibility(View.GONE);
                        btnReject.setVisibility(View.GONE);

                    }
                    int receiverId = jsonNode.get("data").get("Receiver").get("PremisesId").asInt();
                    int senderId = jsonNode.get("data").get("Sender").get("PremisesId").asInt();
                    if(receiverId == providerId) {
                        providerDto.setName(jsonNode.get("data").get("Receiver").get("Name").asText());
                        providerDto.setAddress(jsonNode.get("data").get("Receiver").get("Address").asText());
                    } else if (senderId == providerId){
                        providerDto.setName(jsonNode.get("data").get("Sender").get("Name").asText());
                        providerDto.setAddress(jsonNode.get("data").get("Sender").get("Address").asText());
                    }
                    loadFoodData(foodId);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Object ex) {
                tv.setText("load information that bai");
            }
        });
    }

    public void loadFoodData(final int foodId) {
        FoodService.getInstance().getFoods(ScanResultActivity.this, foodId, new VolleyCallBack() {
            @Override
            public void onSuccess(Object response) {


                try {
                    String json = response.toString();
                    ObjectMapper mapper = new ObjectMapper();
                    JsonNode jsonNode = mapper.readTree(json);


                    farmDto = new Farm();

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
//                                providerDto.setName(objNode.get("Name").asText());
//                                providerDto.setAddress(objNode.get("Address").asText());
                                JsonNode treatment = objNode.get("Treatment");
                                if (!treatment.asText().equals("null")) {
                                    System.out.println(treatment + "         aaaaaaaaaaaaaaaaaaa");
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

            }
        });
    }

    public void showConfirmDialog() {
        TextView txtClose;
        myDialog.setContentView(R.layout.confirm_reason_dialog);
        txtClose = myDialog.findViewById(R.id.txtClose);
        btnConfirm = myDialog.findViewById(R.id.btnConfirm);
        txtClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtReason = myDialog.findViewById(R.id.txtReason);
                reason = txtReason.getText().toString().trim();
                if (status == 4 && reason.length() == 0) {
                    TextView txtReasonValidation = myDialog.findViewById(R.id.txtReasonValidation);
                    txtReasonValidation.setText("Vui lòng nhập lý do");
                } else {
                    confirmTransaction();
                }
            }
        });
        myDialog.show();
    }

    public void confirmTransaction() {


        if (role.equals("Distributor")) {
            FoodService.getInstance().updateDistributorFood(ScanResultActivity.this, premisesId, foodId, new VolleyCallBack() {
                @Override
                public void onSuccess(Object response) {

                }

                @Override
                public void onError(Object ex) {

                }
            });
        }
        TransactionService.getInstance().updateTransaction(ScanResultActivity.this, transactionId, status, reason, role, userID, new VolleyCallBack() {
            @Override
            public void onSuccess(Object response) {

            }

            @Override
            public void onError(Object ex) {

            }
        });


        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }

    public void goToHome(View view) {
        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }
}
