package com.hl7soft.sevenedit.util.licapi.gui;

import com.hl7soft.sevenedit.util.licapi.License;
import com.hl7soft.sevenedit.util.licapi.io.LicenseReader;
import com.hl7soft.sevenedit.util.licapi.io.LicenseWriter;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EtchedBorder;
import javax.swing.text.DateFormatter;
import javax.swing.text.MaskFormatter;

/**
 * @Author Krico
 * @Date 2023/5/4 16:36
 * @Version V1.0
 */
public class LicenseGenerator extends JFrame {
    static boolean packFrame = false;

    JPanel mainPanel = new JPanel();

    JPanel buttonsPanel = new JPanel();

    JMenuBar mainMenu = new JMenuBar();

    JMenu menuFile = new JMenu();

    JMenuItem miFileExit = new JMenuItem();

    JButton btnGenerateKey = new JButton();

    JButton btnReadKey = new JButton();

    JButton btnClearDate = new JButton();

    JLabel lbCustomerId = new JLabel();

    JTextField tfCustomerId = new JTextField();

    JLabel lbProductId = new JLabel();

    JTextField tfProductId = new JTextField();

    JLabel lbFeatureId = new JLabel();

    JTextField tfFeatureId = new JTextField();

    JFormattedTextField tfLicenseKey = new JFormattedTextField();

    JLabel lbExpirationDate = new JLabel();

    JLabel lbExpirationDateLegend = new JLabel();

    JFormattedTextField tfExpirationDate = new JFormattedTextField(new DateFormatter());

    public LicenseGenerator() {
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        setSize(400, 300);
        setResizable(false);
        setTitle("Key Generator");
        this.mainPanel.setBorder(new EtchedBorder());
        this.mainPanel.setLayout(new XYLayout());
        this.btnGenerateKey.setText("Generate Key");
        this.btnGenerateKey.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                LicenseGenerator.this.handleGenerateKey(e);
            }
        });
        this.btnReadKey.setText("Read Key");
        this.btnReadKey.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                LicenseGenerator.this.handleReadKey(e);
            }
        });
        this.btnClearDate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                LicenseGenerator.this.handleClearDate(e);
            }
        });
        this.buttonsPanel.setLayout(new FlowLayout(2));
        this.buttonsPanel.add(this.btnReadKey);
        this.buttonsPanel.add(this.btnGenerateKey);
        this.lbCustomerId.setText("Customer ID");
        this.tfCustomerId.setPreferredSize(new Dimension(150, 20));
        this.lbProductId.setText("Product ID");
        this.tfProductId.setPreferredSize(new Dimension(150, 20));
        this.lbFeatureId.setText("Feature ID");
        this.tfFeatureId.setPreferredSize(new Dimension(50, 20));
        this.tfFeatureId.setText("0");
        this.lbExpirationDate.setText("Expiration Date");
        this.tfExpirationDate.setPreferredSize(new Dimension(80, 20));
        this.tfLicenseKey = new JFormattedTextField(new MaskFormatter("AAAAA-AAAAA-AAAAA-AAAAA"));
        this.tfLicenseKey.setValue("00000-00000-00000-00000");
        this.tfLicenseKey.setPreferredSize(new Dimension(372, 20));
        this.tfExpirationDate.setValue(new Date());
        this.lbExpirationDateLegend.setText("(DD.MM.YYYY)");
        this.btnClearDate.setPreferredSize(new Dimension(70, 20));
        this.btnClearDate.setBorder(new EtchedBorder());
        this.btnClearDate.setFocusPainted(false);
        this.btnClearDate.setText("Clear");
        int labelsOffset = 10;
        int textFieldsOffset = 90;
        this.mainPanel.add(this.lbCustomerId, new Point(labelsOffset, 12));
        this.mainPanel.add(this.tfCustomerId, new Point(textFieldsOffset, 10));
        this.mainPanel.add(this.lbProductId, new Point(labelsOffset, 42));
        this.mainPanel.add(this.tfProductId, new Point(textFieldsOffset, 40));
        this.mainPanel.add(this.lbFeatureId, new Point(labelsOffset, 72));
        this.mainPanel.add(this.tfFeatureId, new Point(textFieldsOffset, 70));
        this.mainPanel.add(this.lbExpirationDate, new Point(labelsOffset, 102));
        this.mainPanel.add(this.tfExpirationDate, new Point(textFieldsOffset, 100));
        this.mainPanel.add(this.btnClearDate, new Point(180, 100));
        this.mainPanel.add(this.tfLicenseKey, new Point(labelsOffset, 185));
        this.mainMenu.add(this.menuFile);
        this.menuFile.setText("File");
        this.miFileExit.setText("Exit");
        this.miFileExit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                LicenseGenerator.this.handleExit(e);
            }
        });
        this.menuFile.add(this.miFileExit);
        setJMenuBar(this.mainMenu);
        getContentPane().add(this.mainPanel, "Center");
        getContentPane().add(this.buttonsPanel, "South");
    }

    void handleExit(ActionEvent e) {
        System.exit(-1);
    }

    void handleClearDate(ActionEvent e) {
        this.tfExpirationDate.setValue((Object)null);
    }

    void handleGenerateKey(ActionEvent e) {
        try {
            String customerIdStr = this.tfCustomerId.getText();
            String productIdStr = this.tfProductId.getText();
            String featureIdStr = this.tfFeatureId.getText();
            if (customerIdStr.length() == 0)
                throw new RuntimeException("Please set customer ID!");
            if (productIdStr.length() == 0)
                throw new RuntimeException("Please set product ID!");
            if (featureIdStr.length() == 0)
                throw new RuntimeException("Please set feature ID!");
            int customerId = 0;
            try {
                customerId = Integer.parseInt(customerIdStr);
            } catch (Exception ex) {
                throw new RuntimeException("Can't parse Customer ID: " + customerId);
            }
            int productId = 0;
            try {
                productId = Integer.parseInt(productIdStr);
            } catch (Exception ex) {
                throw new RuntimeException("Can't parse Product ID: " + productId);
            }
            int featureId = 0;
            try {
                featureId = Integer.parseInt(featureIdStr);
            } catch (Exception ex) {
                throw new RuntimeException("Can't parse Feature ID:" + featureId);
            }
            Calendar expirationDate = null;
            if (this.tfExpirationDate.getValue() != null)
                try {
                    Date date = (Date)this.tfExpirationDate.getValue();
                    expirationDate = Calendar.getInstance();
                    expirationDate.setTime(date);
                } catch (Exception ex) {
                    throw new RuntimeException("Can't parse date: " + expirationDate);
                }
            License license = new License(customerId, productId, expirationDate);
            LicenseWriter writer = new LicenseWriter();
            String key = writer.writeToKey(license, "Demo");
            this.tfLicenseKey.setText(key);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", 0);
        }
    }

    void handleReadKey(ActionEvent e) {
        try {
            String licenseKey = this.tfLicenseKey.getText();
            if (licenseKey.length() == 0)
                throw new RuntimeException("Please set license key!");
            LicenseReader reader = new LicenseReader();
            License license = reader.readFromKey(licenseKey, "Demo");
            this.tfCustomerId.setText("" + license.getSerialNumber());
            this.tfProductId.setText("" + license.getProductId());
            if (license.getExpirationDate() != null) {
                Date date = license.getExpirationDate().getTime();
                this.tfExpirationDate.setValue(date);
            } else {
                this.tfExpirationDate.setValue((Object)null);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", 0);
        }
    }

    protected void processWindowEvent(WindowEvent e) {
        super.processWindowEvent(e);
        if (e.getID() == 201)
            System.exit(-1);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            LicenseGenerator frame = new LicenseGenerator();
            if (packFrame) {
                frame.pack();
            } else {
                frame.validate();
            }
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            Dimension frameSize = frame.getSize();
            if (frameSize.height > screenSize.height)
                frameSize.height = screenSize.height;
            if (frameSize.width > screenSize.width)
                frameSize.width = screenSize.width;
            frame.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
            frame.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
