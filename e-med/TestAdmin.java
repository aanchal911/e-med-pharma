public class TestAdmin {
    public static void main(String[] args) {
        try {
            new com.emedpharma.admin.AdminDashboard("admin01").setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}