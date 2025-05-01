import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EbayCartCheckout extends JFrame {

    private CardLayout cardLayout;
    private JPanel mainPanel;
    private JLabel quantityLabel, subtotalLabel, itemTotalLabel, orderTotalLabel;
    private int quantity = 1;
    private final double price = 49.99;
    private boolean itemSavedForLater = false;

    private JPanel itemPanel;
    private JPanel savedForLaterPanel;

    private JLabel addressLabel;
    private JLabel paymentLabel;

    public EbayCartCheckout() {
        setTitle("Ebay Cart and Checkout");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 600);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        mainPanel.add(createCartPanel(), "Cart");
        mainPanel.add(createCheckoutPanel(), "Checkout");

        add(mainPanel);
        setVisible(true);
    }

    private JPanel createCartPanel() {
        JPanel cartPanel = new JPanel(new BorderLayout(15, 15));
        cartPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        cartPanel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel("Shopping Cart");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setBorder(new EmptyBorder(0, 0, 10, 0));
        cartPanel.add(titleLabel, BorderLayout.NORTH);

        itemPanel = new JPanel();
        itemPanel.setLayout(new BoxLayout(itemPanel, BoxLayout.Y_AXIS));
        itemPanel.setBorder(new CompoundBorder(new LineBorder(Color.GRAY, 1, true), new EmptyBorder(15, 15, 15, 15)));
        itemPanel.setBackground(Color.WHITE);

        JLabel nameLabel = new JLabel("Example Product Name");
        nameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        JLabel sellerLabel = new JLabel("Sold by: example_seller");
        JLabel priceLabel = new JLabel("Price: ₹" + price);

        quantityLabel = new JLabel("Quantity: " + quantity);
        subtotalLabel = new JLabel("Subtotal: ₹" + String.format("%.2f", price * quantity));

        JButton plusButton = createButton("+");
        JButton minusButton = createButton("-");
        JButton removeButton = createButton("Remove");
        JButton saveButton = createButton("Save for later");
        JButton checkoutButton = createMainButton("Proceed to Checkout");

        plusButton.addActionListener(e -> updateQuantity(quantity + 1));
        minusButton.addActionListener(e -> {
            if (quantity > 1) updateQuantity(quantity - 1);
        });
        removeButton.addActionListener(e -> updateQuantity(0));

        saveButton.addActionListener(e -> {
            if (!itemSavedForLater) {
                itemSavedForLater = true;
                updateQuantity(0);
                itemPanel.setVisible(false);
                savedForLaterPanel.setVisible(true);
            }
        });

        checkoutButton.addActionListener(e -> cardLayout.show(mainPanel, "Checkout"));

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT));
        controls.setBackground(Color.WHITE);
        controls.add(minusButton);
        controls.add(plusButton);

        itemPanel.add(nameLabel);
        itemPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        itemPanel.add(sellerLabel);
        itemPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        itemPanel.add(priceLabel);
        itemPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        itemPanel.add(quantityLabel);
        itemPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        itemPanel.add(controls);
        itemPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        itemPanel.add(subtotalLabel);
        itemPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        itemPanel.add(removeButton);
        itemPanel.add(saveButton);

        // Save for later panel
        savedForLaterPanel = new JPanel();
        savedForLaterPanel.setLayout(new BoxLayout(savedForLaterPanel, BoxLayout.Y_AXIS));
        savedForLaterPanel.setBorder(new CompoundBorder(new LineBorder(Color.LIGHT_GRAY), new EmptyBorder(15, 15, 15, 15)));
        savedForLaterPanel.setBackground(Color.WHITE);
        savedForLaterPanel.setVisible(false);

        JLabel savedLabel = new JLabel("Saved for Later:");
        savedLabel.setFont(new Font("Arial", Font.BOLD, 16));
        JLabel savedItemLabel = new JLabel("Example Product Name - ₹" + price);

        JButton addToCartButton = createButton("Add to Cart");
        addToCartButton.addActionListener(e -> {
            itemSavedForLater = false;
            quantity = 1;
            updateQuantity(quantity);
            savedForLaterPanel.setVisible(false);
            itemPanel.setVisible(true);
        });

        savedForLaterPanel.add(savedLabel);
        savedForLaterPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        savedForLaterPanel.add(savedItemLabel);
        savedForLaterPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        savedForLaterPanel.add(addToCartButton);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(Color.WHITE);
        centerPanel.add(itemPanel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        centerPanel.add(savedForLaterPanel);

        cartPanel.add(centerPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.add(checkoutButton);
        cartPanel.add(bottomPanel, BorderLayout.SOUTH);

        return cartPanel;
    }

    private JPanel createCheckoutPanel() {
        JPanel checkoutPanel = new JPanel(new BorderLayout(15, 15));
        checkoutPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        checkoutPanel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel("Checkout");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setBorder(new EmptyBorder(0, 0, 10, 0));
        checkoutPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(Color.WHITE);

        // Shipping Address Panel
        JPanel addressPanel = createInfoPanel("Shipping Address:", "123 Main Street\nCity, State 12345", true);
        addressLabel = (JLabel) addressPanel.getClientProperty("label");

        // Payment Method Panel
        JPanel paymentPanel = createInfoPanel("Payment Method:", "Visa ending in 1234", true);
        paymentLabel = (JLabel) paymentPanel.getClientProperty("label");

        itemTotalLabel = new JLabel("Item Total: ₹" + String.format("%.2f", price * quantity));
        orderTotalLabel = new JLabel("Order Total: ₹" + String.format("%.2f", price * quantity));

        JButton placeOrderButton = createMainButton("Place Order");
        placeOrderButton.addActionListener(e -> {
            double total = price * quantity;
            if (total <= 0.0) {
                JOptionPane.showMessageDialog(this, "Cannot place an order with ₹0.00 total.", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                saveOrderDetails();
                JOptionPane.showMessageDialog(this, "Order placed successfully!");
            }
        });

        JButton homeButton = createButton("Back to Cart");
        homeButton.addActionListener(e -> cardLayout.show(mainPanel, "Cart"));

        centerPanel.add(addressPanel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        centerPanel.add(paymentPanel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        centerPanel.add(itemTotalLabel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        centerPanel.add(orderTotalLabel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        centerPanel.add(placeOrderButton);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        centerPanel.add(homeButton);

        checkoutPanel.add(centerPanel, BorderLayout.CENTER);

        return checkoutPanel;
    }

    private JPanel createInfoPanel(String title, String value, boolean editable) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        JLabel label = new JLabel("<html><b>" + title + "</b><br>" + value.replace("\n", "<br>") + "</html>");
        JButton editButton = createButton("Edit");
        if (editable) {
            editButton.addActionListener(e -> {
                String newValue = JOptionPane.showInputDialog(this, "Enter new " + title.toLowerCase());
                if (newValue != null && !newValue.trim().isEmpty()) {
                    label.setText("<html><b>" + title + "</b><br>" + newValue.replace("\n", "<br>") + "</html>");
                }
            });
        }
        panel.add(label, BorderLayout.CENTER);
        panel.add(editButton, BorderLayout.EAST);
        panel.putClientProperty("label", label);
        return panel;
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(70, 130, 180));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(150, 35));
        return button;
    }

    private JButton createMainButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(34, 139, 34));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(200, 45));
        return button;
    }

    private void updateQuantity(int newQuantity) {
        this.quantity = newQuantity;
        quantityLabel.setText("Quantity: " + quantity);
        subtotalLabel.setText("Subtotal: ₹" + String.format("%.2f", price * quantity));
        itemTotalLabel.setText("Item Total: ₹" + String.format("%.2f", price * quantity));
        orderTotalLabel.setText("Order Total: ₹" + String.format("%.2f", price * quantity));
    }

    private void saveOrderDetails() {
        String orderDetails = "Order Date: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "\n";
        orderDetails += "Item: Example Product Name\n";
        orderDetails += "Quantity: " + quantity + "\n";
        orderDetails += "Subtotal: ₹" + String.format("%.2f", price * quantity) + "\n";
        orderDetails += "Shipping Address: " + addressLabel.getText().replaceAll("<[^>]+>", "").replace("\n", " ") + "\n";
        orderDetails += "Payment Method: " + paymentLabel.getText().replaceAll("<[^>]+>", "") + "\n";
        orderDetails += "---------------------------------------------\n\n";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("orders.txt", true))) {
            writer.write(orderDetails);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving the order.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(EbayCartCheckout::new);
    }
}
