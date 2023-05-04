package com.hl7soft.sevenedit.util.licapi.gui;

import com.hl7soft.sevenedit.util.licapi.License;
import com.hl7soft.sevenedit.util.licapi.io.LicenseReader;
import com.hl7soft.sevenedit.util.licapi.io.LicenseWriter;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.DateFormatter;

public class LicenseGenerator2 extends JFrame {
    private String defaultPassword = "com.7edit.2x";

    private int defaultProduct = 10101;

    private JPanel jContentPane = null;

    private JMenuBar jJMenuBar = null;

    private JMenu fileMenu = null;

    private JMenuItem exitMenuItem = null;

    private JMenuItem saveMenuItem = null;

    private JPanel mainPanel = null;

    private JPanel btnPanel = null;

    private JButton btnGenerate = null;

    private JLabel lbRange = null;

    private JTextField tfRangeStart = null;

    private JTextField tfRangeEnd = null;

    private JLabel lbProduct = null;

    private JTextField tfProduct = null;

    private JLabel lbExpiration = null;

    JFormattedTextField tfExpirationDate = null;

    private JScrollPane spSerials = null;

    private JTextArea taSerials = null;

    private JTextField tfPassword = null;

    private JLabel lbPassword = null;

    JButton decodeButton;

    public LicenseGenerator2() {
        initialize();
    }

    private void initialize() {
        setDefaultCloseOperation(3);
        setJMenuBar(getJJMenuBar());
        setSize(500, 400);
        setContentPane(getJContentPane());
        setTitle("Application");
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(-1);
            }
        });
    }

    private JPanel getJContentPane() {
        if (this.jContentPane == null) {
            this.jContentPane = new JPanel();
            this.jContentPane.setLayout(new BorderLayout());
            this.jContentPane.add(getMainPanel(), "Center");
            this.jContentPane.add(getBtnPanel(), "South");
        }
        return this.jContentPane;
    }

    private JMenuBar getJJMenuBar() {
        if (this.jJMenuBar == null) {
            this.jJMenuBar = new JMenuBar();
            this.jJMenuBar.add(getFileMenu());
        }
        return this.jJMenuBar;
    }

    private JMenu getFileMenu() {
        if (this.fileMenu == null) {
            this.fileMenu = new JMenu();
            this.fileMenu.setText("File");
            this.fileMenu.add(getExitMenuItem());
        }
        return this.fileMenu;
    }

    private JMenuItem getExitMenuItem() {
        if (this.exitMenuItem == null) {
            this.exitMenuItem = new JMenuItem();
            this.exitMenuItem.setText("Exit");
            this.exitMenuItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    System.exit(0);
                }
            });
        }
        return this.exitMenuItem;
    }

    private JPanel getMainPanel() {
        if (this.mainPanel == null) {
            GridBagConstraints gridBagConstraints31 = new GridBagConstraints();
            gridBagConstraints31.gridx = 0;
            gridBagConstraints31.anchor = 17;
            gridBagConstraints31.insets = new Insets(5, 5, 0, 5);
            gridBagConstraints31.gridy = 0;
            this.lbPassword = new JLabel();
            this.lbPassword.setText("Password");
            GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
            gridBagConstraints21.fill = 2;
            gridBagConstraints21.gridy = 0;
            gridBagConstraints21.weightx = 1.0D;
            gridBagConstraints21.insets = new Insets(5, 5, 0, 5);
            gridBagConstraints21.gridx = 1;
            GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
            gridBagConstraints11.fill = 1;
            gridBagConstraints11.gridy = 5;
            gridBagConstraints11.weightx = 1.0D;
            gridBagConstraints11.weighty = 1.0D;
            gridBagConstraints11.insets = new Insets(5, 5, 5, 5);
            gridBagConstraints11.gridwidth = 3;
            gridBagConstraints11.gridx = 0;
            GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
            gridBagConstraints6.fill = 2;
            gridBagConstraints6.gridy = 3;
            gridBagConstraints6.weightx = 1.0D;
            gridBagConstraints6.insets = new Insets(5, 5, 0, 5);
            gridBagConstraints6.gridx = 1;
            GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
            gridBagConstraints5.gridx = 0;
            gridBagConstraints5.anchor = 13;
            gridBagConstraints5.insets = new Insets(5, 5, 0, 5);
            gridBagConstraints5.gridy = 3;
            this.lbExpiration = new JLabel();
            this.lbExpiration.setText("Expiration Date");
            GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
            gridBagConstraints4.fill = 2;
            gridBagConstraints4.gridy = 2;
            gridBagConstraints4.weightx = 1.0D;
            gridBagConstraints4.insets = new Insets(5, 5, 0, 5);
            gridBagConstraints4.gridx = 1;
            GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
            gridBagConstraints3.gridx = 0;
            gridBagConstraints3.anchor = 17;
            gridBagConstraints3.insets = new Insets(5, 5, 0, 5);
            gridBagConstraints3.gridy = 2;
            this.lbProduct = new JLabel();
            this.lbProduct.setText("Product");
            GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
            gridBagConstraints2.fill = 2;
            gridBagConstraints2.gridy = 1;
            gridBagConstraints2.weightx = 1.0D;
            gridBagConstraints2.insets = new Insets(5, 5, 0, 5);
            gridBagConstraints2.gridx = 2;
            GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
            gridBagConstraints1.fill = 2;
            gridBagConstraints1.gridy = 1;
            gridBagConstraints1.weightx = 1.0D;
            gridBagConstraints1.insets = new Insets(5, 5, 0, 5);
            gridBagConstraints1.gridx = 1;
            GridBagConstraints gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.gridx = 0;
            gridBagConstraints.anchor = 17;
            gridBagConstraints.insets = new Insets(5, 5, 0, 5);
            gridBagConstraints.gridy = 1;
            this.lbRange = new JLabel();
            this.lbRange.setText("Serial Range");
            this.mainPanel = new JPanel();
            this.mainPanel.setLayout(new GridBagLayout());
            this.mainPanel
                    .setBorder(
                            BorderFactory.createEtchedBorder(1));
            this.mainPanel.add(this.lbRange, gridBagConstraints);
            this.mainPanel.add(getTfRangeStart(), gridBagConstraints1);
            this.mainPanel.add(getTfRangeEnd(), gridBagConstraints2);
            this.mainPanel.add(this.lbProduct, gridBagConstraints3);
            this.mainPanel.add(getTfProduct(), gridBagConstraints4);
            this.mainPanel.add(this.lbExpiration, gridBagConstraints5);
            this.mainPanel.add(getTfExpirationDate(), gridBagConstraints6);
            this.mainPanel.add(getSpSerials(), gridBagConstraints11);
            this.mainPanel.add(getTfPassword(), gridBagConstraints21);
            this.mainPanel.add(this.lbPassword, gridBagConstraints31);
        }
        return this.mainPanel;
    }

    private JPanel getBtnPanel() {
        if (this.btnPanel == null) {
            this.btnPanel = new JPanel(new BorderLayout());
            JPanel leftPanel = new JPanel(new FlowLayout(0));
            this.btnPanel.add(leftPanel, "West");
            leftPanel.add(getDecodeButton(), (Object)null);
            JPanel rightPanel = new JPanel(new FlowLayout(2));
            this.btnPanel.add(rightPanel, "Center");
            rightPanel.add(getBtnGenerate(), (Object)null);
        }
        return this.btnPanel;
    }

    private JButton getBtnGenerate() {
        if (this.btnGenerate == null) {
            this.btnGenerate = new JButton();
            this.btnGenerate.setText("Generate");
            this.btnGenerate.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    LicenseGenerator2.this.handleGenerateKey(e);
                }
            });
        }
        return this.btnGenerate;
    }

    private JButton getDecodeButton() {
        if (this.decodeButton == null) {
            this.decodeButton = new JButton();
            this.decodeButton.setText("Decode");
            this.decodeButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    LicenseGenerator2.this.handleDecodeKey(e);
                }
            });
        }
        return this.decodeButton;
    }

    void handleGenerateKey(ActionEvent e) {
        try {
            String password = this.tfPassword.getText().trim();
            String rangeStartStr = this.tfRangeStart.getText().trim();
            String rangeEndStr = this.tfRangeEnd.getText().trim();
            String productStr = this.tfProduct.getText().trim();
            if (password.length() == 0)
                throw new RuntimeException("Please set password!");
            if (rangeStartStr.length() == 0)
                throw new RuntimeException("Please set range start!");
            if (productStr.length() == 0)
                throw new RuntimeException("Please set product ID!");
            int rangeStart = 0;
            try {
                rangeStart = Integer.parseInt(rangeStartStr);
            } catch (Exception ex) {
                throw new RuntimeException("Can't parse Range Start: " + rangeStartStr);
            }
            int rangeEnd = -1;
            if (rangeEndStr.length() > 0)
                try {
                    rangeEnd = Integer.parseInt(rangeEndStr);
                } catch (Exception ex) {
                    throw new RuntimeException("Can't parse Range End: " + rangeEndStr);
                }
            int productId = 0;
            try {
                productId = Integer.parseInt(productStr);
            } catch (Exception ex) {
                throw new RuntimeException("Can't parse Product ID: " + productStr);
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
            if (rangeEnd == -1)
                rangeEnd = rangeStart;
            StringBuffer sb = new StringBuffer();
            for (int serial = rangeStart; serial <= rangeEnd; serial++) {
                License license = new License(serial, productId, expirationDate);
                LicenseWriter writer = new LicenseWriter();
                String key = writer.writeToKey(license, password);
                sb.append(key).append("\n");
            }
            this.taSerials.setText(sb.toString());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", 0);
        }
    }

    void handleDecodeKey(ActionEvent e) {
        try {
            String key = this.taSerials.getText();
            key = key.trim();
            if (key.length() == 0) {
                JOptionPane.showMessageDialog(this, "Paste a key first.");
                this.taSerials.grabFocus();
                return;
            }
            if (key.indexOf('\n') != -1) {
                JOptionPane.showMessageDialog(this, "Several keys detected. Paste only one to decode.");
                this.taSerials.grabFocus();
                return;
            }
            String password = this.tfPassword.getText();
            LicenseReader reader = new LicenseReader();
            License license = reader.readFromKey(key, password);
            this.tfProduct.setText("" + license.getProductId());
            if (license.getExpirationDate() != null)
                this.tfExpirationDate.setText((new SimpleDateFormat("dd.MM.yyyy")).format(license.getExpirationDate()));
            this.tfRangeStart.setText("" + license.getSerialNumber());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error decoding key.", 0);
        }
    }

    private JTextField getTfRangeStart() {
        if (this.tfRangeStart == null) {
            this.tfRangeStart = new JTextField();
            this.tfRangeStart.setText("1");
        }
        return this.tfRangeStart;
    }

    private JTextField getTfRangeEnd() {
        if (this.tfRangeEnd == null)
            this.tfRangeEnd = new JTextField();
        return this.tfRangeEnd;
    }

    private JTextField getTfProduct() {
        if (this.tfProduct == null) {
            this.tfProduct = new JTextField();
            this.tfProduct.setText("" + this.defaultProduct);
        }
        return this.tfProduct;
    }

    private JTextField getTfExpirationDate() {
        if (this.tfExpirationDate == null)
            this.tfExpirationDate = new JFormattedTextField(new DateFormatter());
        return this.tfExpirationDate;
    }

    private JScrollPane getSpSerials() {
        if (this.spSerials == null) {
            this.spSerials = new JScrollPane();
            this.spSerials.setViewportView(getTaSerials());
        }
        return this.spSerials;
    }

    private JTextArea getTaSerials() {
        if (this.taSerials == null)
            this.taSerials = new JTextArea();
        return this.taSerials;
    }

    private JTextField getTfPassword() {
        if (this.tfPassword == null)
            this.tfPassword = new JTextField();
        this.tfPassword.setText(this.defaultPassword);
        return this.tfPassword;
    }

    public String getDefaultPassword() {
        return this.defaultPassword;
    }

    public void setDefaultPassword(String defaultPassword) {
        this.defaultPassword = defaultPassword;
        this.tfPassword.setText(defaultPassword);
    }

    public int getDefaultProduct() {
        return this.defaultProduct;
    }

    public void setDefaultProduct(int defaultProduct) {
        this.defaultProduct = defaultProduct;
        this.tfProduct.setText("" + defaultProduct);
    }

    /**
     * 1.产品ID看Edition枚举
     * 2.序列号输入
     * 3.password com.7edit.2x
     * 4.日期输入序列号截止日期
     */
    public static void main(String[] args) {
        LicenseGenerator2 application = new LicenseGenerator2();
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            if (arg.startsWith("password")) {
                int idx = arg.indexOf('=');
                if (idx != -1) {
                    arg = arg.substring(idx + 1);
                    application.setDefaultPassword(arg);
                }
            } else if (arg.startsWith("product")) {
                int idx = arg.indexOf('=');
                if (idx != -1) {
                    arg = arg.substring(idx + 1);
                    application.setDefaultProduct(Integer.parseInt(arg));
                }
            }
        }
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = application.getSize();
        if (frameSize.height > screenSize.height)
            frameSize.height = screenSize.height;
        if (frameSize.width > screenSize.width)
            frameSize.width = screenSize.width;
        application.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
        application.setVisible(true);
    }
}
