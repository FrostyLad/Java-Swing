// BRADEN JOHNSTON 20005898 - 159234 AS3 2022
import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

public class GUI implements Runnable {

    int selected;  // currently selected row

    Computer item;  // selected row's corresponding computer

    List<Staff>  staffList;  // list of staff
    List<Computer> stock;  // list of computers
    List<Computer> stocktable = new ArrayList<>();  // list of computers used to build table, often manipulated by filter function
    AtomicBoolean loggedIn = new AtomicBoolean(false);  // whether login has been successful
    JFrame frame;  // global frame variable

    GUI(List<Staff> staffList, List<Computer> stock) {
        this.staffList = staffList;
        this.stock = stock;
        stocktable.addAll(stock);  // cloned list so they can be manipulated separately

    }

    public class ComputerTable extends AbstractTableModel {

        @Override
        public int getRowCount() {
            return stocktable.size();
        }

        @Override
        public int getColumnCount() {
            return 6;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            Computer item = stocktable.get(rowIndex);
            return switch (columnIndex) {
                case 0 -> item.category;  // item attributes in correct corresponding columns
                case 1 -> item.type;
                case 2 -> item.ID;
                case 3 -> item.brand;
                case 4 -> item.CPU;
                case 5 -> item.price;
                default -> "Invalid item";
            };
        }

        public String getColumnName(int column) {
            return switch (column) {
                case 0 -> "Category";
                case 1 -> "Type";
                case 2 -> "ID";
                case 3 -> "Brand";
                case 4 -> "CPU Family";
                case 5 -> "Price($)";
                default -> "Invalid";
            };
        }
    }

    private void mainSystem(boolean manager) {

        frame.setEnabled(false);  // when called, remove current frame in preparation for a new frame
        frame.setVisible(false);
        frame = new JFrame("Computer Products Management System");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setBounds(10, 10, 600, 900);

        JTabbedPane tabbedPane = new JTabbedPane();
        JPanel browse = new JPanel();
        tabbedPane.addTab("Browse Products", null, browse);  // new tab
        ComputerTable computerTable = new ComputerTable();  // custom table object

        JTable table = new JTable((computerTable));  // JTable with custom values
        table.setPreferredScrollableViewportSize(new Dimension(575, 550));
        JScrollPane scrollPane = new JScrollPane(table);  // adding table to a scrollPane


        JButton button = new JButton("Click to log out", new ImageIcon("cstore.png"));  // Log out button
        button.setHorizontalTextPosition(SwingConstants.RIGHT);
        button.setVerticalTextPosition(SwingConstants.CENTER);
        button.setVerticalAlignment(SwingConstants.BOTTOM);
        button.setFont(new Font("arial-black", Font.BOLD ,22));
        button.setPreferredSize(new Dimension(500, 200));
        ActionListener buttonAL = e -> {  // if logout button is pressed
            frame.setVisible(false);  // kill (kinda) current frame
            frame.setEnabled(false);
            GUI gui = new GUI(staffList, stock);  // start again from login process
            gui.run();
        };
        button.addActionListener(buttonAL);

        String[] categoryOptions = {"All", "Desktop PC", "Laptop", "Tablet"};  // creating category dropdown
        JComboBox<String> category = new JComboBox<>(categoryOptions);
        JComboBox<String> type = new JComboBox<>();  // and type dropdown
        category.setBounds(0, 10, 700, 90);
        type.setBounds(0, 100, 700, 90);

        ActionListener categoryAL = e -> {  // if category dropdown detects a change
            String choice = Objects.requireNonNull(category.getSelectedItem()).toString();  // get the chosen category
            String[] typeOptions = new String[0];

            stocktable.clear();  // clear table

            if (!choice.equals("All")) {  // if anything but 'All' is selected
                for (Computer n : stock) {
                    if (n.category.equals(choice)) {
                        stocktable.add(n);  // add only the computers with a matching category back to the table
                    }
                }
            }
            else { stocktable.addAll(stock);}  // if all is selected, add everything back

            type.removeAllItems();  // remove all options from the type dropdown

            switch (choice) {  // generate new options
                case "Desktop PC" -> typeOptions = new String[]{"All", "Gaming", "Home & Study", "Business", "Compact"};

                case "Laptop" -> typeOptions = new String[]{"All", "Gaming", "Home & Study", "Business", "Thin & Light"};

                case "Tablet" -> typeOptions = new String[]{"All", "Android", "Apple", "Windows"};
            }
            for (String n : typeOptions) {
                type.addItem(n);  // add new options to dropdown
            }
            table.invalidate();  // refresh table
            frame.setVisible(true);  // sometimes fixes visual errors
        };
        category.addActionListener(categoryAL);

        ActionListener typeAL = e -> {  // if type dropdown detects a change
            if (type.getSelectedItem() == null) {  // if nothing got selected, safely return without changing anything
                return;
            }
            String choice = type.getSelectedItem().toString();  // get the chosen type
            stocktable.clear();  // clear table

            for (Computer n : stock) {
                if (n.category.equals(Objects.requireNonNull(category.getSelectedItem()).toString())) {
                    if (n.type.equals(choice) || choice.equals("All")) {
                        stocktable.add(n);  // add computers with matching category and type back to the table
                    }
                }
            }

            table.invalidate();  // refresh table
            frame.setVisible(true);
        };
        type.addActionListener(typeAL);

        JLabel categoryLabel = new JLabel("Computer Category");  // labels for dropdowns
        JLabel typeLabel = new JLabel("Computer Type");
        browse.add(categoryLabel);  // add items to browse tab
        browse.add(category);
        browse.add(typeLabel);
        browse.add(type);
        browse.add(scrollPane);
        browse.add(button);


        Dimension tempdim = new Dimension(270, 35);  // all text boxes are the same dimension
        JPanel update = new JPanel(new GridBagLayout());  // second tab
        GridBagConstraints c = new GridBagConstraints();
        tabbedPane.addTab("Check/Update Products Details", null, update);
        item = stock.get(selected);  // selected computer
        JLabel modelL = new JLabel("Model ID:");  // give all attributes a label and a text field with preferred sizes
        JTextArea modelT = new JTextArea(item.ID);
        modelT.setPreferredSize(tempdim);
        JLabel categoryL = new JLabel("Category:");
        JTextArea categoryT = new JTextArea(item.category);
        categoryT.setPreferredSize(tempdim);
        JLabel typeL = new JLabel("Type:");
        JTextArea typeT = new JTextArea(item.type);
        typeT.setPreferredSize(tempdim);
        JLabel brandL = new JLabel("Brand:");
        JTextArea brandT = new JTextArea(item.brand);
        brandT.setPreferredSize(tempdim);
        JLabel CPUL = new JLabel("CPU Family:");
        JTextArea CPUT = new JTextArea(item.CPU);
        CPUT.setPreferredSize(tempdim);
        JLabel memoryL = new JLabel("Memory Size:");
        JTextArea memoryT = new JTextArea(Integer.toString(item.memory));
        memoryT.setPreferredSize(tempdim);
        JLabel storageL = new JLabel("SSD Capacity:");
        JTextArea storageT = new JTextArea(Integer.toString(item.storage));
        storageT.setPreferredSize(tempdim);
        JLabel screenL = new JLabel("Screen Size:");
        JTextArea screenT = new JTextArea(Float.toString(item.screen));
        screenT.setPreferredSize(tempdim);
        JLabel priceL = new JLabel("Price:");
        JTextArea priceT = new JTextArea(Integer.toString(item.price));
        priceT.setPreferredSize(tempdim);

        table.getSelectionModel().addListSelectionListener(e -> {  // if a new row is selected in the table
            if (table.getSelectedRow() > -1) {  // update second page to match selected item's details
                selected = table.getSelectedRow();
                item = stocktable.get(selected);
                modelT.setText(item.ID);
                categoryT.setText(item.category);
                typeT.setText(item.type);
                brandT.setText(item.brand);
                CPUT.setText(item.CPU);
                memoryT.setText(Integer.toString(item.memory));
                storageT.setText(Integer.toString(item.storage));
                screenT.setText(Float.toString(item.screen));
                priceT.setText(Integer.toString(item.price));
                frame.setVisible(true);

            }
        });

        JButton addB = new JButton("Add");  // add button
        ActionListener addBAL = e -> {  // if add button pressed
            String[] attributes = {categoryT.getText(), typeT.getText(), modelT.getText(), brandT.getText(),
            CPUT.getText(), memoryT.getText(), storageT.getText(), screenT.getText(), priceT.getText()};
            Computer newComp = new Computer(attributes);  // make a new computer with the inputted attributes
            stock.add(newComp);  // add to lists
            stocktable.add(newComp);
            table.invalidate();  // refresh table
        };
        addB.addActionListener(addBAL);

        JButton updateB = new JButton("Update");  // update button
        ActionListener updateBAL = e -> {  // if update button pressed
            for (Computer n : stock) {
                if (n.ID.equals(modelT.getText())) {  // find corresponding computer
                    n.setBrand(brandT.getText());  // change all the attributes respectively
                    n.setCategory(categoryT.getText());
                    n.setCPU(CPUT.getText());
                    n.setID(modelT.getText());
                    n.setMemory(Integer.parseInt(memoryT.getText()));
                    n.setPrice(Integer.parseInt(priceT.getText()));
                    n.setScreen(Float.parseFloat(screenT.getText()));
                    n.setStorage(Integer.parseInt(storageT.getText()));
                    n.setType(typeT.getText());
                }
            }
            table.invalidate();  // refresh table
        };
        updateB.addActionListener(updateBAL);

        JButton deleteB = new JButton("Delete");  // delete button
        ActionListener deleteBAL = e -> {  // if delete button pressed
            stock.removeIf(n -> n.ID.equals(modelT.getText()));  // remove item from lists
            stocktable.removeIf(n -> n.ID.equals(modelT.getText()));
            table.invalidate();  // refresh table
        };
        deleteB.addActionListener(deleteBAL);

        JButton clearB = new JButton("Clear");  // clear button
        ActionListener clearBAL = e -> {  // if clear button pressed
            categoryT.setText("");  // clear all text boxes
            modelT.setText("");
            typeT.setText("");
            brandT.setText("");
            CPUT.setText("");
            memoryT.setText("");
            storageT.setText("");
            screenT.setText("");
            priceT.setText("");
        };
        clearB.addActionListener(clearBAL);

        c.anchor = GridBagConstraints.CENTER;  // draw all items on page in respective cells
        c.gridx = 0;
        c.gridy = 0;
        update.add(modelL, c);
        c.gridx = 1;
        c.gridy = 0;
        update.add(modelT, c);
        c.gridx = 0;
        c.gridy = 1;
        update.add(categoryL, c);
        c.gridx = 1;
        c.gridy = 1;
        update.add(categoryT, c);
        c.gridx = 0;
        c.gridy = 2;
        update.add(typeL, c);
        c.gridx = 1;
        c.gridy = 2;
        update.add(typeT, c);
        c.gridx = 0;
        c.gridy = 3;
        update.add(brandL, c);
        c.gridx = 1;
        c.gridy = 3;
        update.add(brandT, c);
        c.gridx = 0;
        c.gridy = 4;
        update.add(CPUL, c);
        c.gridx = 1;
        c.gridy = 4;
        update.add(CPUT, c);
        c.gridx = 0;
        c.gridy = 5;
        update.add(memoryL, c);
        c.gridx = 1;
        c.gridy = 5;
        update.add(memoryT, c);
        c.gridx = 0;
        c.gridy = 6;
        update.add(storageL, c);
        c.gridx = 1;
        c.gridy = 6;
        update.add(storageT, c);
        c.gridx = 0;
        c.gridy = 7;
        update.add(screenL, c);
        c.gridx = 1;
        c.gridy = 7;
        update.add(screenT, c);
        c.gridx = 0;
        c.gridy = 8;
        update.add(priceL, c);
        c.gridx = 1;
        c.gridy = 8;
        update.add(priceT, c);

        c.gridx = 0;
        c.gridy = 10;
        c.insets = new Insets(10, 0 , 0, 0);  // give a gap between the buttons and the rest of the attributes
        c.fill = GridBagConstraints.HORIZONTAL;
        update.add(addB, c);
        c.gridx = 1;
        c.gridy = 10;
        c.fill = GridBagConstraints.HORIZONTAL;
        update.add(updateB, c);
        c.gridx = 0;
        c.gridy = 11;
        c.fill = GridBagConstraints.HORIZONTAL;
        update.add(deleteB, c);
        c.gridx = 1;
        c.gridy = 11;
        c.fill = GridBagConstraints.HORIZONTAL;
        update.add(clearB, c);


        if (!manager) {  // take functionality away from text boxes and buttons if staff member is not a manager
            update.setEnabled(false);
            modelT.setEditable(false);
            categoryT.setEditable(false);
            typeT.setEditable(false);
            brandT.setEditable(false);
            CPUT.setEditable(false);
            memoryT.setEditable(false);
            storageT.setEditable(false);
            screenT.setEditable(false);
            priceT.setEditable(false);
            addB.setEnabled(false);
            updateB.setEnabled(false);
            deleteB.setEnabled(false);
            clearB.setEnabled(false);

        }
        c.gridx = 0;
        c.gridy = 12;
        c.insets = new Insets(50, 0, 0, 0);  // larger gap between smaller buttons and log out button

        JButton button1 = new JButton("Click to log out", new ImageIcon("cstore.png"));
        button1.setHorizontalTextPosition(SwingConstants.CENTER);
        button1.setVerticalTextPosition(SwingConstants.TOP);
        button1.setVerticalAlignment(SwingConstants.BOTTOM);
        button1.setHorizontalAlignment(SwingConstants.CENTER);
        button1.setFont(new Font("arial-black", Font.BOLD ,22));
        button1.setPreferredSize(new Dimension(300, 200));
        button1.addActionListener(buttonAL);

        update.add(button1, c);


        frame.add(tabbedPane);  // add tabs to frame
        frame.setVisible(true);  // draw frame
    }

    private void Login(JFrame frame) {  // Login prompt
        JLabel userLabel;
        JLabel passLabel;
        JTextField userField;
        JPasswordField passField;
        JButton button;
        JDialog dialog = new JDialog(frame, "Sales Person Login");  // login box
        dialog.setLayout(null);
        dialog.setBounds(600, 300, 500, 200);
        userLabel = new JLabel("Username:");
        userField = new JTextField(20);
        passLabel = new JLabel("Password:");
        passField = new JPasswordField(20);
        button = new JButton("Log In");
        ActionListener AL = e -> {  // if log in button pressed
            String user = userField.getText();  // get entered values
            String pass = String.valueOf(passField.getPassword());
            for (Staff s : staffList) {  // check them against staff's login details to find a match
                if (s.getUser().equals(user)) {
                    if (s.getPass().equals(pass)) {
                        loggedIn.set(true);  // login successful
                        dialog.setEnabled(false);
                        dialog.setVisible(false);
                        mainSystem(s.isManager());  // we enter main section of GUI from here
                    }
                }
            }
            if (!loggedIn.get()) {  // login unsuccessful
                JOptionPane.showMessageDialog(null, "Username or password incorrect.");
                dialog.setEnabled(false);
                dialog.setVisible(false);
            }
        };
        button.addActionListener(AL);  // add labels, text fields and buttons to page
        dialog.add(userLabel);
        dialog.add(userField);
        dialog.add(passLabel);
        dialog.add(passField);
        dialog.add(button);
        userLabel.setLocation(10, 20);
        userLabel.setSize(300, 50);
        userField.setLocation(110, 30);
        userField.setSize(200, 30);
        passLabel.setLocation(10, 60);
        passLabel.setSize(300, 50);
        passField.setLocation(110, 70);
        passField.setSize(200, 30);
        button.setLocation(110,120);
        button.setSize(100,30);
        dialog.setVisible(true);
    }

    @Override
    public void run() {  // when gui is started
        frame = new JFrame("Computer Products Management System");  // create a frame
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBounds(100, 100, 500, 200);

        JButton button = new JButton("Click to login", new ImageIcon("cstore.png"));
        button.setHorizontalTextPosition(SwingConstants.RIGHT);
        button.setVerticalTextPosition(SwingConstants.CENTER);
        button.setBounds(0, 0, 500, 200);
        button.setFont(new Font("arial-black", Font.BOLD ,22));
        ActionListener AL = e -> Login(frame);  // when button is pressed, run login method
        button.addActionListener(AL);
        frame.add(button);
        frame.setVisible(true);  // draw frame

    }
}
// sorry if the code got a bit messy there, I hope you're having a good day!
