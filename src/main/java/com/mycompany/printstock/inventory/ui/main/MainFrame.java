package com.mycompany.printstock.inventory.ui.main;

import com.mycompany.printstock.inventory.service.DashboardService;
import com.mycompany.printstock.inventory.ui.components.*;
import com.mycompany.printstock.inventory.ui.panels.*;
import com.mycompany.printstock.inventory.model.User;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private JPanel contentPanel;
    private JPanel sidebarPanel; 
    private SidebarItem[] menuItems;
    private String activeTab = "dashboard";
    private JLabel headerTitle;
    private DashboardPanel dashboardPanel;
    private BarangPanel barangPanel;
    private StokMasukPanel stokMasukPanel;
    private StokKeluarPanel stokKeluarPanel;
    private LaporanPanel laporanPanel;
    
    private User loggedInUser; 

    public MainFrame(User user) {
        this.loggedInUser = user;

        setTitle("PrintStock - Toko Percetakan");
        setSize(1280, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(248, 250, 252));

        initSidebar();
        initHeader();
        initContent();
        showPanel("dashboard");
    }

    private void initSidebar() {
        sidebarPanel = new JPanel();
        sidebarPanel.setBackground(Color.WHITE);
        sidebarPanel.setPreferredSize(new Dimension(256, 0));
        sidebarPanel.setLayout(new BorderLayout());
        sidebarPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(226, 232, 240)));
        
        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 16));
        logoPanel.setOpaque(false);
        logoPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(241, 245, 249)));

        JPanel iconBox = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(15, 23, 42));
                g2.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, 8, 8);
                g2.dispose();
            }
        };
        iconBox.setOpaque(false);
        iconBox.setPreferredSize(new Dimension(32, 32));
        iconBox.setLayout(new GridBagLayout());
        iconBox.add(new JLabel(LucideIcon.createIcon(LucideIcon.IconName.STORE, 16, Color.WHITE)));

        JPanel textPanel = new JPanel(new GridLayout(2, 1));
        textPanel.setOpaque(false);
        JLabel title = new JLabel("PrintStock");
        title.setFont(new Font("Inter", Font.BOLD, 14));
        title.setForeground(new Color(15, 23, 42));
        JLabel sub = new JLabel("Toko Percetakan");
        sub.setFont(new Font("Inter", Font.PLAIN, 11));
        sub.setForeground(new Color(100, 116, 139));
        textPanel.add(title);
        textPanel.add(sub);

        logoPanel.add(iconBox);
        logoPanel.add(textPanel);
        sidebarPanel.add(logoPanel, BorderLayout.NORTH);

        JPanel menuPanel = new JPanel();
        menuPanel.setOpaque(false);
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        String[][] menus = {
            {"dashboard", "Dashboard", "LAYOUT_DASHBOARD"},
            {"barang", "Data Barang", "PACKAGE"},
            {"stok-masuk", "Stok Masuk", "ARROW_DOWN_LEFT"},
            {"stok-keluar", "Stok Keluar", "ARROW_UP_RIGHT"},
            {"laporan", "Laporan", "FILE_TEXT"}
        };

        menuItems = new SidebarItem[menus.length];
        for (int i = 0; i < menus.length; i++) {
            LucideIcon.IconName icon = LucideIcon.IconName.valueOf(menus[i][2]);
            SidebarItem item = new SidebarItem(LucideIcon.createIcon(icon, 18, new Color(100, 116, 139)), menus[i][1]);
            final String tab = menus[i][0];
            item.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    showPanel(tab);
                }
            });
            menuPanel.add(item);
            menuPanel.add(Box.createVerticalStrut(4));
            menuItems[i] = item;
        }
        sidebarPanel.add(menuPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setOpaque(false);
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
        bottomPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(241, 245, 249)));

        // --- Status Info ---
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 12));
        statusPanel.setOpaque(false);

        GlassPanel statusGlass = new GlassPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        statusGlass.setRadius(12);
        statusGlass.setBackground(new Color(248, 250, 252));
        statusGlass.setBorderColor(new Color(241, 245, 249));
        statusGlass.setPreferredSize(new Dimension(220, 60));

        statusPanel.add(statusGlass);
        bottomPanel.add(statusPanel);

        // --- Logout Button ---
//        JPanel logoutPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 8));
//        logoutPanel.setOpaque(false);
//
//        ModernButton logoutBtn = new ModernButton("Logout");
//        logoutBtn.setBgColor(new Color(239, 68, 68));
//        logoutBtn.setForeground(Color.WHITE);
//        logoutBtn.setRadius(8);
//        logoutBtn.setPreferredSize(new Dimension(220, 36));
//        logoutBtn.addActionListener(e -> {
//            int confirm = JOptionPane.showConfirmDialog(this,
//                "Apakah Anda yakin ingin keluar?", "Konfirmasi Logout",
//                JOptionPane.YES_NO_OPTION);
//
//            if (confirm == JOptionPane.YES_OPTION) {
//                this.dispose();
//                new LoginFrame().setVisible(true);
//            }
//        });
//        logoutPanel.add(logoutBtn);
//        bottomPanel.add(logoutPanel);

        sidebarPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(sidebarPanel, BorderLayout.WEST);
    }

    private void initHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(255, 255, 255, 240));
        header.setPreferredSize(new Dimension(0, 64));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(226, 232, 240)));

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 12));
        left.setOpaque(false);

        ModernButton menuBtn = new ModernButton(LucideIcon.createIcon(LucideIcon.IconName.MENU, 20, new Color(71, 85, 105)), "");
        menuBtn.setGhost(true);
        menuBtn.setPreferredSize(new Dimension(40, 40));
        
        menuBtn.addActionListener(e -> {
            sidebarPanel.setVisible(!sidebarPanel.isVisible());
        });

        headerTitle = new JLabel("Dashboard");
        headerTitle.setFont(new Font("Inter", Font.BOLD, 16));
        headerTitle.setForeground(new Color(15, 23, 42));

        left.add(menuBtn);
        left.add(headerTitle);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 16, 12));
        right.setOpaque(false);

        ModernButton notifBtn = new ModernButton(LucideIcon.createIcon(LucideIcon.IconName.BELL, 20, new Color(71, 85, 105)), "");
        notifBtn.setGhost(true);
        notifBtn.setPreferredSize(new Dimension(40, 40));
        notifBtn.addActionListener(e -> showNotifications(notifBtn));

        JPanel profileContainer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        profileContainer.setOpaque(false);

        JPanel userTextPanel = new JPanel(new GridLayout(2, 1, 0, 2));
        userTextPanel.setOpaque(false);

        String namaUser = (loggedInUser != null) ? loggedInUser.getNama() : "Unknown User";
        String roleUser = (loggedInUser != null) ? loggedInUser.getRole() : "Staff";

        boolean isConnected = false;
        try {
            var conn = com.mycompany.printstock.inventory.dao.DatabaseManager.getInstance().getConnection();
            if (conn != null && !conn.isClosed()) {
                isConnected = true;
            }
        } catch (Exception e) {
            isConnected = false;
        }

        String dbStatusText = isConnected 
            ? "<span style='color:#10B981;'>● Connected</span>" 
            : "<span style='color:#EF4444;'>● Disconnected</span>";

        JLabel nameLabel = new JLabel(namaUser + " (" + roleUser + ")");
        nameLabel.setFont(new Font("Inter", Font.BOLD, 13));
        nameLabel.setForeground(new Color(15, 23, 42));
        nameLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        JLabel statusLabel = new JLabel("<html><span style='color:#64748B'>Database: </span>" + dbStatusText + "</html>");
        statusLabel.setFont(new Font("Inter", Font.PLAIN, 11));
        statusLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        userTextPanel.add(nameLabel);
        userTextPanel.add(statusLabel);

        JPanel avatar = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
            }
        };
        avatar.setOpaque(false);
        avatar.setPreferredSize(new Dimension(36, 36));

        profileContainer.add(userTextPanel);
        profileContainer.add(avatar);

        right.add(notifBtn);
        right.add(profileContainer);

        header.add(left, BorderLayout.WEST);
        header.add(right, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);
    }

    private void initContent() {
        contentPanel = new JPanel(new CardLayout());
        contentPanel.setOpaque(false);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        dashboardPanel = new DashboardPanel();
        barangPanel = new BarangPanel();
        stokMasukPanel = new StokMasukPanel();
        stokKeluarPanel = new StokKeluarPanel();
        laporanPanel = new LaporanPanel();

        contentPanel.add(dashboardPanel, "dashboard");
        contentPanel.add(barangPanel, "barang");
        contentPanel.add(stokMasukPanel, "stok-masuk");
        contentPanel.add(stokKeluarPanel, "stok-keluar");
        contentPanel.add(laporanPanel, "laporan");

        add(contentPanel, BorderLayout.CENTER);
    }

    private void showPanel(String tab) {
        this.activeTab = tab;
        CardLayout cl = (CardLayout) contentPanel.getLayout();
        cl.show(contentPanel, tab);

        String[] titles = {"Dashboard", "Data Barang", "Stok Masuk", "Stok Keluar", "Laporan"};
        String[] tabs = {"dashboard", "barang", "stok-masuk", "stok-keluar", "laporan"};
        for (int i = 0; i < tabs.length; i++) {
            if (tabs[i].equals(tab)) {
                headerTitle.setText(titles[i]);
                menuItems[i].setActive(true);
            } else {
                menuItems[i].setActive(false);
            }
        }

        if (tab.equals("dashboard")) dashboardPanel.refreshData();
        else if (tab.equals("barang")) barangPanel.refreshData();
        else if (tab.equals("stok-masuk")) stokMasukPanel.refreshData();
        else if (tab.equals("stok-keluar")) stokKeluarPanel.refreshData();
        else if (tab.equals("laporan")) laporanPanel.refreshData();
    }

    private void showNotifications(Component anchor) {
        try {
            DashboardService svc = new DashboardService();
            NotificationPopup popup = new NotificationPopup(svc.getStokRendah(), svc.getStokTinggi());
            popup.show(anchor, 0, anchor.getHeight() + 8);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}