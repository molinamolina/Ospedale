/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package core.views;

import core.ApplicationContext;
import core.controllers.responses.Response;
import core.models.entities.Administrator;
import core.models.entities.Doctor;
import core.models.entities.Specialty;
import core.models.entities.User;
import core.observers.implementations.TableObserver;
import core.utils.EntitySerializer;
import core.utils.SpecialtyMapper;
import java.awt.Color;
import java.util.HashMap;
import java.util.List;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author jjlora
 * @author edangulo
 */
public class DoctorView extends javax.swing.JFrame {

    private int x, y;
    private final ApplicationContext context;
    private final User user;
    private Doctor doctor;

    public DoctorView(User user, Doctor doctor) {
        initComponents();
        this.context = ApplicationContext.getInstance();
        this.user = user;
        this.doctor = doctor;
        btnBackToAdmin.setVisible(user instanceof Administrator);
        this.setBackground(new Color(0, 0, 0, 0));
        this.setLocationRelativeTo(null);
        loadDoctorProfile();
        loadSpecialties();
        loadPatientSearchCombo();
        loadAppointmentCombos();
        loadHospitalizationCombo();
        refreshDoctorAppointmentsTable(false);
        context.getStorage().attach(new TableObserver(() -> refreshDoctorAppointmentsTable(rdbDoctorPendingAppointments.isSelected())));
    }

    private void loadDoctorProfile() {
        txtDoctorFirstname.setText(doctor.getFirstname());
        txtDoctorLastname.setText(doctor.getLastname());
        cmbDoctorSpecialty.setSelectedItem(SpecialtyMapper.toDisplayName(doctor.getSpecialty()));
        txtDoctorLicenceNumber.setText(doctor.getLicenceNumber());
        txtDoctorAssignedOffice.setText(doctor.getAssignedOffice());
        txtDoctorUsername.setText(doctor.getUsername());
        txtDoctorPassword.setText(doctor.getPassword());
        txtDoctorConfirmPassword.setText(doctor.getPassword());
    }

    private void loadSpecialties() {
        cmbDoctorSpecialty.removeAllItems();
        for (Specialty specialty : Specialty.values()) {
            cmbDoctorSpecialty.addItem(SpecialtyMapper.toDisplayName(specialty));
        }
        cmbDoctorSpecialty.setSelectedItem(SpecialtyMapper.toDisplayName(doctor.getSpecialty()));
    }

    private void loadPatientSearchCombo() {
        cmbSearchPatient.removeAllItems();
        cmbSearchPatient.addItem("Select one");
        Response response = context.getPatientController().getAllPatients();
        if (response.getData() == null) {
            return;
        }
        List<HashMap<String, Object>> patients = (List<HashMap<String, Object>>) response.getData().get("patients");
        for (HashMap<String, Object> patient : patients) {
            cmbSearchPatient.addItem(patient.get("id") + " - " + patient.get("label"));
        }
    }

    private void loadAppointmentCombos() {
        Response response = context.getAppointmentController().getDoctorAppointments(doctor.getId(), false);
        cmbAcceptAppointmentId.removeAllItems();
        cmbRescheduleAppointmentId.removeAllItems();
        cmbCompleteAppointmentId.removeAllItems();
        cmbPrescriptionAppointment.removeAllItems();
        cmbAcceptAppointmentId.addItem("Select one");
        cmbRescheduleAppointmentId.addItem("Select one");
        cmbCompleteAppointmentId.addItem("Select one");
        cmbPrescriptionAppointment.addItem("Select one");
        cmbHospitalizationAppointment.removeAllItems();
        cmbHospitalizationAppointment.addItem("Select one");
        if (response.getData() == null) {
            return;
        }
        List<HashMap<String, Object>> appointments = (List<HashMap<String, Object>>) response.getData().get("appointments");
        for (HashMap<String, Object> appointment : appointments) {
            String id = String.valueOf(appointment.get("id"));
            String status = String.valueOf(appointment.get("status"));
            if ("REQUESTED".equals(status)) {
                cmbAcceptAppointmentId.addItem(id);
                cmbRescheduleAppointmentId.addItem(id);
            }
            if ("PENDING".equals(status)) {
                cmbCompleteAppointmentId.addItem(id);
                cmbPrescriptionAppointment.addItem(id);
                cmbHospitalizationAppointment.addItem(id);
            }
        }
    }

    private void loadHospitalizationCombo() {
        cmbHospitalizationRequestId.removeAllItems();
        cmbHospitalizationRequestId.addItem("Select one");
        Response response = context.getHospitalizationController().getDoctorHospitalizations(doctor.getId());
        if (response.getData() == null) {
            return;
        }
        List<HashMap<String, Object>> hospitalizations = (List<HashMap<String, Object>>) response.getData().get("hospitalizations");
        for (HashMap<String, Object> hospitalization : hospitalizations) {
            if ("REQUESTED".equals(hospitalization.get("status"))) {
                cmbHospitalizationRequestId.addItem(String.valueOf(hospitalization.get("id")));
            }
        }
    }

    private void refreshDoctorAppointmentsTable(boolean pendingOnly) {
        Response response = context.getAppointmentController().getDoctorAppointments(doctor.getId(), pendingOnly);
        DefaultTableModel model = (DefaultTableModel) tblDoctorAppointments.getModel();
        model.setRowCount(0);
        if (response.getData() == null) {
            return;
        }
        List<HashMap<String, Object>> appointments = (List<HashMap<String, Object>>) response.getData().get("appointments");
        for (HashMap<String, Object> appointment : appointments) {
            model.addRow(new Object[]{
                appointment.get("id"),
                appointment.get("datetime"),
                appointment.get("patientName"),
                appointment.get("specialty"),
                appointment.get("type"),
                appointment.get("status")
            });
        }
        loadAppointmentCombos();
        loadHospitalizationCombo();
    }

    private void refreshPatientAppointmentsTable(long patientId) {
        Response response = context.getAppointmentController().getPatientAppointments(patientId);
        DefaultTableModel model = (DefaultTableModel) tblSearchPatientAppointments.getModel();
        model.setRowCount(0);
        if (response.getData() == null) {
            return;
        }
        List<HashMap<String, Object>> appointments = (List<HashMap<String, Object>>) response.getData().get("appointments");
        for (HashMap<String, Object> appointment : appointments) {
            model.addRow(new Object[]{
                appointment.get("id"),
                appointment.get("datetime"),
                appointment.get("doctorName"),
                appointment.get("specialty"),
                appointment.get("type"),
                appointment.get("status")
            });
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlBackground = new packagee.PanelRound();
        pnlTitleBar = new packagee.PanelRound();
        btnCloseWindow = new javax.swing.JButton();
        lblDoctorFirstname = new javax.swing.JLabel();
        btnBackToAdmin = new javax.swing.JButton();
        tabDoctorMain = new javax.swing.JTabbedPane();
        pnlPrescriptions = new javax.swing.JPanel();
        rdbDoctorAllAppointments = new javax.swing.JRadioButton();
        scrSearchPatientAppointments = new javax.swing.JScrollPane();
        tblDoctorAppointments = new javax.swing.JTable();
        rdbDoctorPendingAppointments = new javax.swing.JRadioButton();
        btnLogout = new javax.swing.JButton();
        pnlSearchPatient = new javax.swing.JPanel();
        cmbSearchPatient = new javax.swing.JComboBox<>();
        lblPrescriptionFrequency = new javax.swing.JLabel();
        scrDoctorAppointments = new javax.swing.JScrollPane();
        tblSearchPatientAppointments = new javax.swing.JTable();
        btnSearchPatientAppointments = new javax.swing.JButton();
        pnlProfile = new javax.swing.JPanel();
        lblDoctorLastname = new javax.swing.JLabel();
        txtDoctorFirstname = new javax.swing.JTextField();
        lblDoctorTitle = new javax.swing.JLabel();
        txtDoctorLastname = new javax.swing.JTextField();
        lblDoctorSpecialty = new javax.swing.JLabel();
        lblDoctorLicenceNumber = new javax.swing.JLabel();
        txtDoctorLicenceNumber = new javax.swing.JTextField();
        lblDoctorAssignedOffice = new javax.swing.JLabel();
        txtDoctorUsername = new javax.swing.JTextField();
        lblDoctorUsername = new javax.swing.JLabel();
        txtDoctorAssignedOffice = new javax.swing.JTextField();
        txtDoctorPassword = new javax.swing.JTextField();
        lblDoctorPassword = new javax.swing.JLabel();
        lblDoctorConfirmPassword = new javax.swing.JLabel();
        txtDoctorConfirmPassword = new javax.swing.JTextField();
        cmbDoctorSpecialty = new javax.swing.JComboBox<>();
        btnUpdateProfile = new javax.swing.JButton();
        pnlAppointmentActions = new javax.swing.JPanel();
        lblAcceptAppointmentId = new javax.swing.JLabel();
        lblAcceptSection = new javax.swing.JLabel();
        cmbAcceptAppointmentId = new javax.swing.JComboBox<>();
        sepAcceptReschedule = new javax.swing.JSeparator();
        btnAcceptAppointment = new javax.swing.JButton();
        lblRescheduleSection = new javax.swing.JLabel();
        lblRescheduleAppointmentId = new javax.swing.JLabel();
        cmbRescheduleAppointmentId = new javax.swing.JComboBox<>();
        btnRescheduleAppointment = new javax.swing.JButton();
        lblRescheduleTime = new javax.swing.JLabel();
        txtRescheduleTime = new javax.swing.JTextField();
        lblRescheduleReason = new javax.swing.JLabel();
        txtRescheduleReason = new javax.swing.JTextField();
        sepComplete = new javax.swing.JSeparator();
        lblCompleteSection = new javax.swing.JLabel();
        lblCompleteAppointmentId = new javax.swing.JLabel();
        cmbCompleteAppointmentId = new javax.swing.JComboBox<>();
        lblCompleteDiagnosis = new javax.swing.JLabel();
        lblCompleteObservations = new javax.swing.JLabel();
        lblCompleteTreatment = new javax.swing.JLabel();
        lblCompleteFollowUp = new javax.swing.JLabel();
        btnCompleteAppointment = new javax.swing.JButton();
        lblHospitalizationSection = new javax.swing.JLabel();
        lblHospitalizationReason = new javax.swing.JLabel();
        lblHospitalizationEntryDate = new javax.swing.JLabel();
        txtHospitalizationEntryDate = new javax.swing.JTextField();
        lblHospitalizationDuration = new javax.swing.JLabel();
        txtHospitalizationDuration = new javax.swing.JTextField();
        lblHospitalizationObservations = new javax.swing.JLabel();
        scrHospitalizationObservations = new javax.swing.JScrollPane();
        txtAreaHospitalizationObservations = new javax.swing.JTextArea();
        btnGenerateHospitalization = new javax.swing.JButton();
        cmbHospitalizationRequestId = new javax.swing.JComboBox<>();
        rdbHospitalizationRequests = new javax.swing.JRadioButton();
        rdbHospitalizationByAppointment = new javax.swing.JRadioButton();
        scrCompleteDiagnosis = new javax.swing.JScrollPane();
        txtAreaCompleteDiagnosis = new javax.swing.JTextArea();
        scrCompleteObservations = new javax.swing.JScrollPane();
        txtAreaCompleteObservations = new javax.swing.JTextArea();
        scrCompleteTreatment = new javax.swing.JScrollPane();
        txtAreaCompleteTreatment = new javax.swing.JTextArea();
        scrCompleteFollowUp = new javax.swing.JScrollPane();
        txtAreaCompleteFollowUp = new javax.swing.JTextArea();
        sepHospitalization = new javax.swing.JSeparator();
        btnDenyHospitalization = new javax.swing.JButton();
        cmbHospitalizationAppointment = new javax.swing.JComboBox<>();
        scrHospitalizationReason = new javax.swing.JScrollPane();
        txtAreaHospitalizationReason = new javax.swing.JTextArea();
        pnlAppointmentsList = new javax.swing.JPanel();
        lblHospitalizationReasonField = new javax.swing.JLabel();
        lblPrescriptionAppointment = new javax.swing.JLabel();
        txtPrescriptionMedication = new javax.swing.JTextField();
        lblPrescriptionMedication = new javax.swing.JLabel();
        txtPrescriptionDose = new javax.swing.JTextField();
        lblPrescriptionDose = new javax.swing.JLabel();
        txtPrescriptionRoute = new javax.swing.JTextField();
        lblPrescriptionRoute = new javax.swing.JLabel();
        txtPrescriptionFrequency = new javax.swing.JTextField();
        lblPrescriptionDuration = new javax.swing.JLabel();
        txtPrescriptionDuration = new javax.swing.JTextField();
        lblPrescriptionInstructions = new javax.swing.JLabel();
        txtPrescriptionInstructions = new javax.swing.JTextField();
        scrPrescriptions = new javax.swing.JScrollPane();
        tblPrescriptions = new javax.swing.JTable();
        btnAddPrescription = new javax.swing.JButton();
        btnClearPrescriptions = new javax.swing.JButton();
        cmbPrescriptionAppointment = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        pnlBackground.setRadius(50);

        pnlTitleBar.setRadius(50);
        pnlTitleBar.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                pnlTitleBarMouseDragged(evt);
            }
        });
        pnlTitleBar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                pnlTitleBarMousePressed(evt);
            }
        });

        btnCloseWindow.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        btnCloseWindow.setText("X");
        btnCloseWindow.setBorderPainted(false);
        btnCloseWindow.setContentAreaFilled(false);
        btnCloseWindow.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btnCloseWindow.setFocusable(false);
        btnCloseWindow.setRequestFocusEnabled(false);
        btnCloseWindow.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseWindowActionPerformed(evt);
            }
        });

        lblDoctorFirstname.setFont(new java.awt.Font("Yu Gothic UI", 0, 14)); // NOI18N
        lblDoctorFirstname.setText("DOCTOR VIEW");

        btnBackToAdmin.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        btnBackToAdmin.setText("Back");
        btnBackToAdmin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackToAdminActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelRound2Layout = new javax.swing.GroupLayout(pnlTitleBar);
        pnlTitleBar.setLayout(panelRound2Layout);
        panelRound2Layout.setHorizontalGroup(
            panelRound2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelRound2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblDoctorFirstname)
                .addGap(32, 32, 32)
                .addComponent(btnBackToAdmin)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnCloseWindow)
                .addGap(19, 19, 19))
        );
        panelRound2Layout.setVerticalGroup(
            panelRound2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelRound2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(btnCloseWindow, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblDoctorFirstname, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(btnBackToAdmin))
        );

        rdbDoctorAllAppointments.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        rdbDoctorAllAppointments.setText("Total appointments");
        rdbDoctorAllAppointments.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdbDoctorAllAppointmentsActionPerformed(evt);
            }
        });

        tblDoctorAppointments.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Date", "Patient", "Specialty", "Type", "Status"
            }
        ));
        scrSearchPatientAppointments.setViewportView(tblDoctorAppointments);

        rdbDoctorPendingAppointments.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        rdbDoctorPendingAppointments.setText("Pending appointments");
        rdbDoctorPendingAppointments.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdbDoctorPendingAppointmentsActionPerformed(evt);
            }
        });

        btnLogout.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        btnLogout.setText("Logout");
        btnLogout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLogoutActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(pnlPrescriptions);
        pnlPrescriptions.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnLogout)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel4Layout.createSequentialGroup()
                            .addGap(16, 16, 16)
                            .addComponent(rdbDoctorAllAppointments)
                            .addGap(18, 18, 18)
                            .addComponent(rdbDoctorPendingAppointments))
                        .addGroup(jPanel4Layout.createSequentialGroup()
                            .addGap(108, 108, 108)
                            .addComponent(scrSearchPatientAppointments, javax.swing.GroupLayout.PREFERRED_SIZE, 1035, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(152, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rdbDoctorAllAppointments)
                    .addComponent(rdbDoctorPendingAppointments))
                .addGap(18, 18, 18)
                .addComponent(scrSearchPatientAppointments, javax.swing.GroupLayout.PREFERRED_SIZE, 504, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 15, Short.MAX_VALUE)
                .addComponent(btnLogout)
                .addGap(23, 23, 23))
        );

        tabDoctorMain.addTab("Appointments visualization", pnlPrescriptions);

        cmbSearchPatient.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        cmbSearchPatient.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select one" }));

        lblPrescriptionFrequency.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        lblPrescriptionFrequency.setText("Patient");

        tblSearchPatientAppointments.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Date", "Doctor", "Specialty", "Type", "Status"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        scrDoctorAppointments.setViewportView(tblSearchPatientAppointments);

        btnSearchPatientAppointments.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        btnSearchPatientAppointments.setText("Search");
        btnSearchPatientAppointments.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchPatientAppointmentsActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(pnlSearchPatient);
        pnlSearchPatient.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(37, 37, 37)
                        .addComponent(lblPrescriptionFrequency)
                        .addGap(18, 18, 18)
                        .addComponent(cmbSearchPatient, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(63, 63, 63)
                        .addComponent(scrDoctorAppointments, javax.swing.GroupLayout.PREFERRED_SIZE, 1133, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(99, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btnSearchPatientAppointments)
                .addGap(601, 601, 601))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblPrescriptionFrequency)
                    .addComponent(cmbSearchPatient, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(scrDoctorAppointments, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(44, 44, 44)
                .addComponent(btnSearchPatientAppointments)
                .addContainerGap(67, Short.MAX_VALUE))
        );

        tabDoctorMain.addTab("History Appointments of a patient", pnlSearchPatient);

        lblDoctorLastname.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        lblDoctorLastname.setText("Firstname");

        txtDoctorFirstname.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N

        lblDoctorTitle.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        lblDoctorTitle.setText("Lastname");

        txtDoctorLastname.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N

        lblDoctorSpecialty.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        lblDoctorSpecialty.setText("Specialty");

        lblDoctorLicenceNumber.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        lblDoctorLicenceNumber.setText("License Number");

        txtDoctorLicenceNumber.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N

        lblDoctorAssignedOffice.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        lblDoctorAssignedOffice.setText("Assigned office");

        txtDoctorUsername.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N

        lblDoctorUsername.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        lblDoctorUsername.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblDoctorUsername.setText("User");

        txtDoctorAssignedOffice.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N

        txtDoctorPassword.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N

        lblDoctorPassword.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        lblDoctorPassword.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblDoctorPassword.setText("Password");

        lblDoctorConfirmPassword.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        lblDoctorConfirmPassword.setText("Password confirmation");

        txtDoctorConfirmPassword.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N

        cmbDoctorSpecialty.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        cmbDoctorSpecialty.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select one", "General Medicine", "Cardiology", "Pediatrics", "Neurology", "Traumatology & Orthopedics", "Gynecology & Obstetrics", "Dermatology", "Psychiatry", "Oncology", "Ophthalmology", "Internal Medicine" }));

        btnUpdateProfile.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        btnUpdateProfile.setText("Save");
        btnUpdateProfile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateProfileActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(pnlProfile);
        pnlProfile.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(211, 211, 211)
                        .addComponent(lblDoctorLastname)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtDoctorFirstname, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lblDoctorTitle)
                        .addGap(18, 18, 18)
                        .addComponent(txtDoctorLastname, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lblDoctorSpecialty)
                        .addGap(18, 18, 18)
                        .addComponent(cmbDoctorSpecialty, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(351, 351, 351)
                        .addComponent(lblDoctorLicenceNumber)
                        .addGap(18, 18, 18)
                        .addComponent(txtDoctorLicenceNumber, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lblDoctorAssignedOffice)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtDoctorAssignedOffice, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(558, 558, 558)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtDoctorPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(txtDoctorUsername, javax.swing.GroupLayout.DEFAULT_SIZE, 109, Short.MAX_VALUE)
                                .addComponent(lblDoctorUsername, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(lblDoctorPassword, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(521, 521, 521)
                        .addComponent(lblDoctorConfirmPassword))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(576, 576, 576)
                        .addComponent(btnUpdateProfile))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(561, 561, 561)
                        .addComponent(txtDoctorConfirmPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(269, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(49, 49, 49)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblDoctorLastname)
                    .addComponent(txtDoctorFirstname, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblDoctorTitle)
                    .addComponent(txtDoctorLastname, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblDoctorSpecialty)
                    .addComponent(cmbDoctorSpecialty, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblDoctorLicenceNumber)
                    .addComponent(txtDoctorLicenceNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtDoctorAssignedOffice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblDoctorAssignedOffice))
                .addGap(30, 30, 30)
                .addComponent(lblDoctorUsername)
                .addGap(18, 18, 18)
                .addComponent(txtDoctorUsername, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lblDoctorPassword)
                .addGap(27, 27, 27)
                .addComponent(txtDoctorPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lblDoctorConfirmPassword)
                .addGap(18, 18, 18)
                .addComponent(txtDoctorConfirmPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(32, 32, 32)
                .addComponent(btnUpdateProfile)
                .addContainerGap(161, Short.MAX_VALUE))
        );

        tabDoctorMain.addTab("Modify info", pnlProfile);

        lblAcceptAppointmentId.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        lblAcceptAppointmentId.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblAcceptAppointmentId.setText("Appointment ID");

        lblAcceptSection.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        lblAcceptSection.setText("Accept medical appointment");

        cmbAcceptAppointmentId.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        cmbAcceptAppointmentId.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select one" }));

        sepAcceptReschedule.setOrientation(javax.swing.SwingConstants.VERTICAL);

        btnAcceptAppointment.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        btnAcceptAppointment.setText("Accept");
        btnAcceptAppointment.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAcceptAppointmentActionPerformed(evt);
            }
        });

        lblRescheduleSection.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        lblRescheduleSection.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblRescheduleSection.setText("Reschedule medical appointment");

        lblRescheduleAppointmentId.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        lblRescheduleAppointmentId.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblRescheduleAppointmentId.setText("Appointment");

        cmbRescheduleAppointmentId.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        cmbRescheduleAppointmentId.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select one" }));

        btnRescheduleAppointment.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        btnRescheduleAppointment.setText("Accept");
        btnRescheduleAppointment.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRescheduleAppointmentActionPerformed(evt);
            }
        });

        lblRescheduleTime.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        lblRescheduleTime.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblRescheduleTime.setText("New time appointment");

        txtRescheduleTime.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N

        lblRescheduleReason.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        lblRescheduleReason.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblRescheduleReason.setText("Reason for appointment");

        txtRescheduleReason.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N

        sepComplete.setOrientation(javax.swing.SwingConstants.VERTICAL);

        lblCompleteSection.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        lblCompleteSection.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblCompleteSection.setText("Complete medical appointment");

        lblCompleteAppointmentId.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        lblCompleteAppointmentId.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblCompleteAppointmentId.setText("Appointment");

        cmbCompleteAppointmentId.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        cmbCompleteAppointmentId.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select one" }));

        lblCompleteDiagnosis.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        lblCompleteDiagnosis.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblCompleteDiagnosis.setText("Diagnosis");

        lblCompleteObservations.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        lblCompleteObservations.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblCompleteObservations.setText("Observations");

        lblCompleteTreatment.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        lblCompleteTreatment.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblCompleteTreatment.setText("Recommended treatment");

        lblCompleteFollowUp.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        lblCompleteFollowUp.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblCompleteFollowUp.setText("Follow-up indication");

        btnCompleteAppointment.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        btnCompleteAppointment.setText("Complete");
        btnCompleteAppointment.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCompleteAppointmentActionPerformed(evt);
            }
        });

        lblHospitalizationSection.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        lblHospitalizationSection.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblHospitalizationSection.setText("Hospitalization");

        lblHospitalizationReason.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        lblHospitalizationReason.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblHospitalizationReason.setText("Reason for hospitalization");

        lblHospitalizationEntryDate.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        lblHospitalizationEntryDate.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblHospitalizationEntryDate.setText("Date of entry");

        txtHospitalizationEntryDate.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N

        lblHospitalizationDuration.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        lblHospitalizationDuration.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblHospitalizationDuration.setText("Estimated duration");

        txtHospitalizationDuration.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N

        lblHospitalizationObservations.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        lblHospitalizationObservations.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblHospitalizationObservations.setText("Observations");

        txtAreaHospitalizationObservations.setColumns(20);
        txtAreaHospitalizationObservations.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        txtAreaHospitalizationObservations.setRows(5);
        scrHospitalizationObservations.setViewportView(txtAreaHospitalizationObservations);

        btnGenerateHospitalization.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        btnGenerateHospitalization.setText("Generate");
        btnGenerateHospitalization.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGenerateHospitalizationActionPerformed(evt);
            }
        });

        cmbHospitalizationRequestId.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        cmbHospitalizationRequestId.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select one" }));

        rdbHospitalizationRequests.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        rdbHospitalizationRequests.setText("Requests");

        rdbHospitalizationByAppointment.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        rdbHospitalizationByAppointment.setText("Patient ID");

        txtAreaCompleteDiagnosis.setColumns(20);
        txtAreaCompleteDiagnosis.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        txtAreaCompleteDiagnosis.setRows(5);
        scrCompleteDiagnosis.setViewportView(txtAreaCompleteDiagnosis);

        txtAreaCompleteObservations.setColumns(20);
        txtAreaCompleteObservations.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        txtAreaCompleteObservations.setRows(5);
        scrCompleteObservations.setViewportView(txtAreaCompleteObservations);

        txtAreaCompleteTreatment.setColumns(20);
        txtAreaCompleteTreatment.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        txtAreaCompleteTreatment.setRows(5);
        scrCompleteTreatment.setViewportView(txtAreaCompleteTreatment);

        txtAreaCompleteFollowUp.setColumns(20);
        txtAreaCompleteFollowUp.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        txtAreaCompleteFollowUp.setRows(5);
        scrCompleteFollowUp.setViewportView(txtAreaCompleteFollowUp);

        sepHospitalization.setOrientation(javax.swing.SwingConstants.VERTICAL);

        btnDenyHospitalization.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        btnDenyHospitalization.setText("Cancel");
        btnDenyHospitalization.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDenyHospitalizationActionPerformed(evt);
            }
        });

        cmbHospitalizationAppointment.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        cmbHospitalizationAppointment.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select one" }));

        txtAreaHospitalizationReason.setColumns(20);
        txtAreaHospitalizationReason.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        txtAreaHospitalizationReason.setRows(5);
        scrHospitalizationReason.setViewportView(txtAreaHospitalizationReason);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(pnlAppointmentActions);
        pnlAppointmentActions.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addGap(26, 26, 26)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                        .addComponent(btnAcceptAppointment)
                                        .addGap(87, 87, 87))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                        .addComponent(cmbAcceptAppointmentId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(67, 67, 67))))
                            .addComponent(lblAcceptAppointmentId, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(sepAcceptReschedule, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addComponent(lblAcceptSection)
                        .addGap(22, 22, 22)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblRescheduleSection, javax.swing.GroupLayout.PREFERRED_SIZE, 306, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(lblRescheduleAppointmentId, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 305, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblRescheduleTime, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 304, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblRescheduleReason, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 303, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addGap(90, 90, 90)
                                    .addComponent(cmbRescheduleAppointmentId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addGap(99, 99, 99)
                                    .addComponent(txtRescheduleTime, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addGap(98, 98, 98)
                                    .addComponent(txtRescheduleReason, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addGap(112, 112, 112)
                                    .addComponent(btnRescheduleAppointment)))
                            .addGap(91, 91, 91))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(sepComplete, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(112, 112, 112)
                        .addComponent(btnCompleteAppointment)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(lblCompleteAppointmentId, javax.swing.GroupLayout.PREFERRED_SIZE, 301, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(lblCompleteSection, javax.swing.GroupLayout.PREFERRED_SIZE, 307, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(99, 99, 99)
                                        .addComponent(cmbCompleteAppointmentId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(0, 25, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblCompleteDiagnosis, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 301, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblCompleteObservations, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 301, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(lblCompleteFollowUp, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(lblCompleteTreatment, javax.swing.GroupLayout.DEFAULT_SIZE, 292, Short.MAX_VALUE))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(42, 42, 42)
                                        .addComponent(scrCompleteDiagnosis, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(41, 41, 41)
                                        .addComponent(scrCompleteObservations, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(42, 42, 42)
                                        .addComponent(scrCompleteTreatment, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(43, 43, 43)
                                        .addComponent(scrCompleteFollowUp, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                .addComponent(sepHospitalization, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(7, 7, 7)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblHospitalizationSection, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(lblHospitalizationEntryDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(lblHospitalizationDuration, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(lblHospitalizationObservations, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(121, 121, 121)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtHospitalizationEntryDate, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtHospitalizationDuration, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addContainerGap())
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(45, 45, 45)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(btnDenyHospitalization)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnGenerateHospitalization))
                            .addComponent(scrHospitalizationObservations, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(56, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(cmbHospitalizationRequestId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(37, 37, 37)
                                .addComponent(rdbHospitalizationRequests)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(rdbHospitalizationByAppointment, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(19, 19, 19))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(cmbHospitalizationAppointment, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(29, 29, 29))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblHospitalizationReason, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(scrHospitalizationReason, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(47, 47, 47))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(sepAcceptReschedule)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(sepComplete)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(lblCompleteSection)
                        .addGap(10, 10, 10)
                        .addComponent(lblCompleteAppointmentId)
                        .addGap(18, 18, 18)
                        .addComponent(cmbCompleteAppointmentId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lblCompleteDiagnosis)
                        .addGap(18, 18, 18)
                        .addComponent(scrCompleteDiagnosis, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lblCompleteObservations)
                        .addGap(18, 18, 18)
                        .addComponent(scrCompleteObservations, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lblCompleteTreatment)
                        .addGap(18, 18, 18)
                        .addComponent(scrCompleteTreatment, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblCompleteFollowUp)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(scrCompleteFollowUp, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnCompleteAppointment)
                        .addGap(12, 12, 12))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(17, 17, 17)
                                .addComponent(lblAcceptSection)
                                .addGap(18, 18, 18)
                                .addComponent(lblAcceptAppointmentId)
                                .addGap(18, 18, 18)
                                .addComponent(cmbAcceptAppointmentId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(31, 31, 31)
                                .addComponent(btnAcceptAppointment))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(19, 19, 19)
                                .addComponent(lblRescheduleSection)
                                .addGap(18, 18, 18)
                                .addComponent(lblRescheduleAppointmentId)
                                .addGap(18, 18, 18)
                                .addComponent(cmbRescheduleAppointmentId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(lblRescheduleTime)
                                .addGap(18, 18, 18)
                                .addComponent(txtRescheduleTime, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(lblRescheduleReason)
                                .addGap(18, 18, 18)
                                .addComponent(txtRescheduleReason, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(30, 30, 30)
                                .addComponent(btnRescheduleAppointment)))
                        .addGap(18, 18, Short.MAX_VALUE))))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(lblHospitalizationSection)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rdbHospitalizationRequests)
                    .addComponent(rdbHospitalizationByAppointment))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbHospitalizationRequestId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbHospitalizationAppointment, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(lblHospitalizationReason)
                .addGap(16, 16, 16)
                .addComponent(scrHospitalizationReason, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblHospitalizationEntryDate)
                .addGap(18, 18, 18)
                .addComponent(txtHospitalizationEntryDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lblHospitalizationDuration)
                .addGap(18, 18, 18)
                .addComponent(txtHospitalizationDuration, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lblHospitalizationObservations)
                .addGap(18, 18, 18)
                .addComponent(scrHospitalizationObservations, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnGenerateHospitalization)
                    .addComponent(btnDenyHospitalization))
                .addGap(0, 0, Short.MAX_VALUE))
            .addComponent(sepHospitalization, javax.swing.GroupLayout.Alignment.TRAILING)
        );

        tabDoctorMain.addTab("Request/Appointments", pnlAppointmentActions);

        lblHospitalizationReasonField.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        lblHospitalizationReasonField.setText("Appointment ID");

        lblPrescriptionAppointment.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        lblPrescriptionAppointment.setText("Medication name");

        txtPrescriptionMedication.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N

        lblPrescriptionMedication.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        lblPrescriptionMedication.setText("Dose");

        txtPrescriptionDose.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N

        lblPrescriptionDose.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        lblPrescriptionDose.setText("Administration route");

        txtPrescriptionRoute.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N

        lblPrescriptionRoute.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        lblPrescriptionRoute.setText("Frecuency");

        txtPrescriptionFrequency.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N

        lblPrescriptionDuration.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        lblPrescriptionDuration.setText("Treatment duration");

        txtPrescriptionDuration.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N

        lblPrescriptionInstructions.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        lblPrescriptionInstructions.setText("Additional instructions");

        txtPrescriptionInstructions.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N

        tblPrescriptions.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Appointment ID", "Medication name", "Dose", "Administration route", "Treatment duration", "Additional instructions", "Frecuency"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        scrPrescriptions.setViewportView(tblPrescriptions);

        btnAddPrescription.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        btnAddPrescription.setText("Add");
        btnAddPrescription.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddPrescriptionActionPerformed(evt);
            }
        });

        btnClearPrescriptions.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        btnClearPrescriptions.setText("Prescribe");
        btnClearPrescriptions.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearPrescriptionsActionPerformed(evt);
            }
        });

        cmbPrescriptionAppointment.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        cmbPrescriptionAppointment.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select one" }));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(pnlAppointmentsList);
        pnlAppointmentsList.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(62, 62, 62)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(scrPrescriptions, javax.swing.GroupLayout.PREFERRED_SIZE, 1125, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(lblHospitalizationReasonField)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(cmbPrescriptionAppointment, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(9, 9, 9)
                                        .addComponent(lblPrescriptionAppointment))
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(lblPrescriptionDuration)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(txtPrescriptionDuration, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(lblPrescriptionInstructions)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(txtPrescriptionInstructions, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(lblPrescriptionRoute)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(txtPrescriptionFrequency, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(txtPrescriptionMedication, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(lblPrescriptionMedication)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(txtPrescriptionDose, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(lblPrescriptionDose)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(txtPrescriptionRoute, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnAddPrescription))))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(583, 583, 583)
                        .addComponent(btnClearPrescriptions)))
                .addContainerGap(108, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(57, 57, 57)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblHospitalizationReasonField)
                    .addComponent(lblPrescriptionAppointment)
                    .addComponent(txtPrescriptionMedication, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblPrescriptionMedication)
                    .addComponent(txtPrescriptionDose, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblPrescriptionDose)
                    .addComponent(txtPrescriptionRoute, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAddPrescription)
                    .addComponent(cmbPrescriptionAppointment, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblPrescriptionDuration)
                    .addComponent(txtPrescriptionDuration, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblPrescriptionInstructions)
                    .addComponent(txtPrescriptionInstructions, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblPrescriptionRoute)
                    .addComponent(txtPrescriptionFrequency, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addComponent(scrPrescriptions, javax.swing.GroupLayout.PREFERRED_SIZE, 340, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(47, 47, 47)
                .addComponent(btnClearPrescriptions)
                .addContainerGap(64, Short.MAX_VALUE))
        );

        tabDoctorMain.addTab("Prescribe medications", pnlAppointmentsList);

        javax.swing.GroupLayout panelRound1Layout = new javax.swing.GroupLayout(pnlBackground);
        pnlBackground.setLayout(panelRound1Layout);
        panelRound1Layout.setHorizontalGroup(
            panelRound1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelRound1Layout.createSequentialGroup()
                .addGroup(panelRound1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(pnlTitleBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(tabDoctorMain))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        panelRound1Layout.setVerticalGroup(
            panelRound1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelRound1Layout.createSequentialGroup()
                .addComponent(pnlTitleBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tabDoctorMain))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlBackground, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlBackground, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void pnlTitleBarMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelRound2MousePressed
        x = evt.getX();
        y = evt.getY();
    }//GEN-LAST:event_panelRound2MousePressed

    private void pnlTitleBarMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelRound2MouseDragged
        this.setLocation(this.getLocation().x + evt.getX() - x, this.getLocation().y + evt.getY() - y);
    }//GEN-LAST:event_panelRound2MouseDragged

    private void btnCloseWindowActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        System.exit(0);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void rdbDoctorPendingAppointmentsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton4ActionPerformed
        rdbDoctorAllAppointments.setSelected(false);
        refreshDoctorAppointmentsTable(true);
    }//GEN-LAST:event_jRadioButton4ActionPerformed

    private void btnUpdateProfileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        HashMap<String, String> data = new HashMap<>();
        data.put("currentId", String.valueOf(doctor.getId()));
        data.put("firstname", txtDoctorFirstname.getText());
        data.put("lastname", txtDoctorLastname.getText());
        data.put("specialty", cmbDoctorSpecialty.getSelectedItem().toString());
        data.put("licenceNumber", txtDoctorLicenceNumber.getText());
        data.put("assignedOffice", txtDoctorAssignedOffice.getText());
        data.put("username", txtDoctorUsername.getText());
        data.put("password", txtDoctorPassword.getText());
        data.put("confirmPassword", txtDoctorConfirmPassword.getText());

        ViewHelper.handleResponse(this, context.getDoctorController().updateDoctor(data), () -> {
            this.doctor = (Doctor) context.findUserById(doctor.getId());
            loadDoctorProfile();
        });
    }//GEN-LAST:event_jButton9ActionPerformed

    private void btnLogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
        context.setCurrentUser(null);
        this.setVisible(false);
        new LoginView().setVisible(true);
    }//GEN-LAST:event_jButton12ActionPerformed

    private void btnBackToAdminActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        this.setVisible(false);
        new AdminView(user).setVisible(true);
    }//GEN-LAST:event_jButton11ActionPerformed

    private void btnDenyHospitalizationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed
        if (!rdbHospitalizationRequests.isSelected() || cmbHospitalizationRequestId.getSelectedIndex() <= 0) {
            return;
        }
        HashMap<String, String> data = new HashMap<>();
        data.put("hospitalizationId", cmbHospitalizationRequestId.getSelectedItem().toString());
        ViewHelper.handleResponse(this, context.getHospitalizationController().denyHospitalization(data), () -> {
            loadHospitalizationCombo();
        });
    }//GEN-LAST:event_jButton13ActionPerformed

    private void btnGenerateHospitalizationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        if (rdbHospitalizationRequests.isSelected()) {
            if (cmbHospitalizationRequestId.getSelectedIndex() <= 0) {
                return;
            }
            HashMap<String, String> data = new HashMap<>();
            data.put("hospitalizationId", cmbHospitalizationRequestId.getSelectedItem().toString());
            ViewHelper.handleResponse(this, context.getHospitalizationController().approveHospitalization(data), () -> {
                loadHospitalizationCombo();
            });
            return;
        }
        if (!rdbHospitalizationByAppointment.isSelected() || cmbHospitalizationAppointment.getSelectedIndex() <= 0) {
            return;
        }
        HashMap<String, String> data = new HashMap<>();
        data.put("appointmentId", cmbHospitalizationAppointment.getSelectedItem().toString());
        data.put("date", txtHospitalizationEntryDate.getText());
        data.put("reason", txtAreaHospitalizationReason.getText());
        data.put("observations", txtAreaHospitalizationObservations.getText());
        data.put("roomType", txtHospitalizationDuration.getText().isBlank() ? "IMC" : txtHospitalizationDuration.getText());
        ViewHelper.handleResponse(this, context.getHospitalizationController().generateFromAppointment(data), () -> {
            txtHospitalizationEntryDate.setText("");
            txtAreaHospitalizationObservations.setText("");
            txtAreaHospitalizationReason.setText("");
            refreshDoctorAppointmentsTable(rdbDoctorPendingAppointments.isSelected());
        });
    }//GEN-LAST:event_jButton6ActionPerformed

    private void btnSearchPatientAppointmentsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        if (cmbSearchPatient.getSelectedIndex() <= 0) {
            return;
        }
        long patientId = Long.parseLong(cmbSearchPatient.getSelectedItem().toString().split(" - ")[0]);
        refreshPatientAppointmentsTable(patientId);
    }//GEN-LAST:event_jButton8ActionPerformed

    private void rdbDoctorAllAppointmentsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton3ActionPerformed
        rdbDoctorPendingAppointments.setSelected(false);
        refreshDoctorAppointmentsTable(false);
    }//GEN-LAST:event_jRadioButton3ActionPerformed

    private void btnAcceptAppointmentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        if (cmbAcceptAppointmentId.getSelectedIndex() <= 0) {
            return;
        }
        HashMap<String, String> data = new HashMap<>();
        data.put("appointmentId", cmbAcceptAppointmentId.getSelectedItem().toString());
        data.put("doctorId", String.valueOf(doctor.getId()));
        ViewHelper.handleResponse(this, context.getAppointmentController().acceptAppointment(data), () -> {
            refreshDoctorAppointmentsTable(rdbDoctorPendingAppointments.isSelected());
        });
    }//GEN-LAST:event_jButton3ActionPerformed

    private void btnCompleteAppointmentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        if (cmbCompleteAppointmentId.getSelectedIndex() <= 0) {
            return;
        }
        HashMap<String, String> data = new HashMap<>();
        data.put("appointmentId", cmbCompleteAppointmentId.getSelectedItem().toString());
        data.put("doctorId", String.valueOf(doctor.getId()));
        data.put("diagnosis", txtAreaCompleteDiagnosis.getText());
        data.put("observations", txtAreaCompleteObservations.getText());
        data.put("recommendedTreatment", txtAreaCompleteTreatment.getText());
        data.put("followUp", txtAreaCompleteFollowUp.getText());
        ViewHelper.handleResponse(this, context.getAppointmentController().completeAppointment(data), () -> {
            txtAreaCompleteDiagnosis.setText("");
            txtAreaCompleteObservations.setText("");
            txtAreaCompleteTreatment.setText("");
            txtAreaCompleteFollowUp.setText("");
            refreshDoctorAppointmentsTable(rdbDoctorPendingAppointments.isSelected());
        });
    }//GEN-LAST:event_jButton5ActionPerformed

    private void btnClearPrescriptionsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        DefaultTableModel model = (DefaultTableModel) tblPrescriptions.getModel();
        model.setRowCount(0);
    }//GEN-LAST:event_jButton10ActionPerformed

    private void btnAddPrescriptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        if (cmbPrescriptionAppointment.getSelectedIndex() <= 0) {
            return;
        }
        HashMap<String, String> data = new HashMap<>();
        data.put("appointmentId", cmbPrescriptionAppointment.getSelectedItem().toString());
        data.put("doctorId", String.valueOf(doctor.getId()));
        data.put("medicationName", txtPrescriptionMedication.getText());
        data.put("dose", txtPrescriptionDose.getText());
        data.put("administrationRoute", txtPrescriptionRoute.getText());
        data.put("treatmentDuration", txtPrescriptionDuration.getText());
        data.put("additionalInstructions", txtPrescriptionInstructions.getText());
        data.put("frequency", txtPrescriptionFrequency.getText());

        ViewHelper.handleResponse(this, context.getAppointmentController().prescribeMedications(data), () -> {
            DefaultTableModel model = (DefaultTableModel) tblPrescriptions.getModel();
            model.addRow(new Object[]{
                cmbPrescriptionAppointment.getSelectedItem(),
                txtPrescriptionMedication.getText(),
                txtPrescriptionDose.getText(),
                txtPrescriptionRoute.getText(),
                txtPrescriptionDuration.getText(),
                txtPrescriptionInstructions.getText(),
                txtPrescriptionFrequency.getText()
            });
            txtPrescriptionMedication.setText("");
            txtPrescriptionDose.setText("");
            txtPrescriptionRoute.setText("");
            txtPrescriptionFrequency.setText("");
            txtPrescriptionDuration.setText("");
            txtPrescriptionInstructions.setText("");
        });
    }//GEN-LAST:event_jButton7ActionPerformed

    private void btnRescheduleAppointmentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        if (cmbRescheduleAppointmentId.getSelectedIndex() <= 0) {
            return;
        }
        HashMap<String, String> data = new HashMap<>();
        data.put("appointmentId", cmbRescheduleAppointmentId.getSelectedItem().toString());
        data.put("newTime", txtRescheduleTime.getText());
        data.put("reason", txtRescheduleReason.getText());
        ViewHelper.handleResponse(this, context.getAppointmentController().rescheduleAppointment(data), () -> {
            txtRescheduleTime.setText("");
            txtRescheduleReason.setText("");
            refreshDoctorAppointmentsTable(rdbDoctorPendingAppointments.isSelected());
        });
    }//GEN-LAST:event_jButton4ActionPerformed




    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCloseWindow;
    private javax.swing.JButton btnClearPrescriptions;
    private javax.swing.JButton btnBackToAdmin;
    private javax.swing.JButton btnLogout;
    private javax.swing.JButton btnDenyHospitalization;
    private javax.swing.JButton btnAcceptAppointment;
    private javax.swing.JButton btnRescheduleAppointment;
    private javax.swing.JButton btnCompleteAppointment;
    private javax.swing.JButton btnGenerateHospitalization;
    private javax.swing.JButton btnAddPrescription;
    private javax.swing.JButton btnSearchPatientAppointments;
    private javax.swing.JButton btnUpdateProfile;
    private javax.swing.JComboBox<String> cmbDoctorSpecialty;
    private javax.swing.JComboBox<String> cmbAcceptAppointmentId;
    private javax.swing.JComboBox<String> cmbRescheduleAppointmentId;
    private javax.swing.JComboBox<String> cmbCompleteAppointmentId;
    private javax.swing.JComboBox<String> cmbSearchPatient;
    private javax.swing.JComboBox<String> cmbHospitalizationRequestId;
    private javax.swing.JComboBox<String> cmbPrescriptionAppointment;
    private javax.swing.JComboBox<String> cmbHospitalizationAppointment;
    private javax.swing.JLabel lblDoctorFirstname;
    private javax.swing.JLabel lblDoctorPassword;
    private javax.swing.JLabel lblDoctorConfirmPassword;
    private javax.swing.JLabel lblAcceptSection;
    private javax.swing.JLabel lblAcceptAppointmentId;
    private javax.swing.JLabel lblRescheduleSection;
    private javax.swing.JLabel lblRescheduleAppointmentId;
    private javax.swing.JLabel lblRescheduleTime;
    private javax.swing.JLabel lblRescheduleReason;
    private javax.swing.JLabel lblCompleteSection;
    private javax.swing.JLabel lblDoctorLastname;
    private javax.swing.JLabel lblCompleteAppointmentId;
    private javax.swing.JLabel lblCompleteDiagnosis;
    private javax.swing.JLabel lblCompleteObservations;
    private javax.swing.JLabel lblCompleteTreatment;
    private javax.swing.JLabel lblCompleteFollowUp;
    private javax.swing.JLabel lblHospitalizationSection;
    private javax.swing.JLabel lblHospitalizationReason;
    private javax.swing.JLabel lblHospitalizationEntryDate;
    private javax.swing.JLabel lblHospitalizationDuration;
    private javax.swing.JLabel lblDoctorTitle;
    private javax.swing.JLabel lblHospitalizationObservations;
    private javax.swing.JLabel lblHospitalizationReasonField;
    private javax.swing.JLabel lblPrescriptionAppointment;
    private javax.swing.JLabel lblPrescriptionMedication;
    private javax.swing.JLabel lblPrescriptionDose;
    private javax.swing.JLabel lblPrescriptionRoute;
    private javax.swing.JLabel lblPrescriptionDuration;
    private javax.swing.JLabel lblPrescriptionInstructions;
    private javax.swing.JLabel lblPrescriptionFrequency;
    private javax.swing.JLabel lblDoctorSpecialty;
    private javax.swing.JLabel lblDoctorLicenceNumber;
    private javax.swing.JLabel lblDoctorAssignedOffice;
    private javax.swing.JLabel lblDoctorUsername;
    private javax.swing.JPanel pnlAppointmentActions;
    private javax.swing.JPanel pnlAppointmentsList;
    private javax.swing.JPanel pnlProfile;
    private javax.swing.JPanel pnlPrescriptions;
    private javax.swing.JPanel pnlSearchPatient;
    private javax.swing.JRadioButton rdbDoctorAllAppointments;
    private javax.swing.JRadioButton rdbDoctorPendingAppointments;
    private javax.swing.JRadioButton rdbHospitalizationRequests;
    private javax.swing.JRadioButton rdbHospitalizationByAppointment;
    private javax.swing.JScrollPane scrHospitalizationObservations;
    private javax.swing.JScrollPane scrHospitalizationReason;
    private javax.swing.JScrollPane scrPrescriptions;
    private javax.swing.JScrollPane scrSearchPatientAppointments;
    private javax.swing.JScrollPane scrDoctorAppointments;
    private javax.swing.JScrollPane scrCompleteDiagnosis;
    private javax.swing.JScrollPane scrCompleteObservations;
    private javax.swing.JScrollPane scrCompleteTreatment;
    private javax.swing.JScrollPane scrCompleteFollowUp;
    private javax.swing.JSeparator sepAcceptReschedule;
    private javax.swing.JSeparator sepComplete;
    private javax.swing.JSeparator sepHospitalization;
    private javax.swing.JTabbedPane tabDoctorMain;
    private javax.swing.JTable tblPrescriptions;
    private javax.swing.JTable tblDoctorAppointments;
    private javax.swing.JTable tblSearchPatientAppointments;
    private javax.swing.JTextArea txtAreaHospitalizationObservations;
    private javax.swing.JTextArea txtAreaCompleteDiagnosis;
    private javax.swing.JTextArea txtAreaCompleteObservations;
    private javax.swing.JTextArea txtAreaCompleteTreatment;
    private javax.swing.JTextArea txtAreaCompleteFollowUp;
    private javax.swing.JTextArea txtAreaHospitalizationReason;
    private javax.swing.JTextField txtDoctorFirstname;
    private javax.swing.JTextField txtDoctorConfirmPassword;
    private javax.swing.JTextField txtRescheduleTime;
    private javax.swing.JTextField txtRescheduleReason;
    private javax.swing.JTextField txtDoctorLastname;
    private javax.swing.JTextField txtHospitalizationEntryDate;
    private javax.swing.JTextField txtHospitalizationDuration;
    private javax.swing.JTextField txtPrescriptionMedication;
    private javax.swing.JTextField txtPrescriptionDose;
    private javax.swing.JTextField txtPrescriptionRoute;
    private javax.swing.JTextField txtPrescriptionFrequency;
    private javax.swing.JTextField txtPrescriptionDuration;
    private javax.swing.JTextField txtPrescriptionInstructions;
    private javax.swing.JTextField txtDoctorLicenceNumber;
    private javax.swing.JTextField txtDoctorUsername;
    private javax.swing.JTextField txtDoctorAssignedOffice;
    private javax.swing.JTextField txtDoctorPassword;
    private packagee.PanelRound pnlBackground;
    private packagee.PanelRound pnlTitleBar;
    // End of variables declaration//GEN-END:variables
}
