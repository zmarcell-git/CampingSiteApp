package Test;

import org.junit.*;
import java.util.List;
import Service.CampingSiteManager;
import Model.CampingSite;
import Model.CampingType;

public class CampingSiteManagerTest {
    
    private CampingSiteManager campingSiteManager;
    
    @Before
    public void setUp() {
        campingSiteManager = new CampingSiteManager();
    }
    
    @Test
    public void testCreateCampingSite() {
        campingSiteManager.CreateCampingSite(CampingType.TENT, 4, 10000);
        
        Assert.assertEquals("Should have 1 camping site", 1, campingSiteManager.getCampingSites().size());
    }
    
    @Test
    public void testCreateMultipleCampingSites() {
        campingSiteManager.CreateCampingSite(CampingType.TENT, 4, 10000);
        campingSiteManager.CreateCampingSite(CampingType.CARAVAN, 6, 15000);
        campingSiteManager.CreateCampingSite(CampingType.CABIN, 8, 20000);
        
        Assert.assertEquals("Should have 3 camping sites", 3, campingSiteManager.getCampingSites().size());
    }
    
    @Test
    public void testAddCampingSite() {
        CampingSite site = new CampingSite(CampingType.CABIN, 5, 12000);
        campingSiteManager.AddCampingSite(site);
        
        Assert.assertEquals("Should have 1 camping site", 1, campingSiteManager.getCampingSites().size());
        Assert.assertEquals("Site should be in the list", site, campingSiteManager.getCampingSites().get(0));
    }
    
    @Test
    public void testGetCampingSiteById() {
        CampingSite site = new CampingSite(CampingType.TENT, 4, 10000);
        campingSiteManager.AddCampingSite(site);
        
        CampingSite retrievedSite = campingSiteManager.getCampingSiteById(site.getId());
        
        Assert.assertNotNull("Site should be found", retrievedSite);
        Assert.assertEquals("Retrieved site should match added site", site.getId(), retrievedSite.getId());
    }
    
    @Test
    public void testGetCampingSiteByIdNotFound() {
        CampingSite retrievedSite = campingSiteManager.getCampingSiteById("NonExistentId");
        
        Assert.assertNull("Site should not be found", retrievedSite);
    }
    
    @Test
    public void testModifyCapacityValid() {
        CampingSite site = new CampingSite(CampingType.TENT, 4, 10000);
        campingSiteManager.AddCampingSite(site);
        
        campingSiteManager.ModifyCapacity(site, "6");
        
        Assert.assertEquals("Capacity should be updated to 6", 6, site.getCapacity());
    }
    
    @Test
    public void testModifyCapacityInvalidInput() {
        CampingSite site = new CampingSite(CampingType.TENT, 4, 10000);
        campingSiteManager.AddCampingSite(site);
        
        campingSiteManager.ModifyCapacity(site, "invalid");
        
        // Capacity should remain unchanged
        Assert.assertEquals("Capacity should remain 4", 4, site.getCapacity());
    }
    
    @Test
    public void testModifyCapacityZero() {
        CampingSite site = new CampingSite(CampingType.TENT, 4, 10000);
        campingSiteManager.AddCampingSite(site);
        
        campingSiteManager.ModifyCapacity(site, "0");
        
        Assert.assertEquals("Capacity should be set to 0", 0, site.getCapacity());
    }
    
    @Test
    public void testModifyCapacityNegative() {
        CampingSite site = new CampingSite(CampingType.TENT, 4, 10000);
        campingSiteManager.AddCampingSite(site);
        
        campingSiteManager.ModifyCapacity(site, "-5");
        
        Assert.assertEquals("Capacity should be set to -5", -5, site.getCapacity());
    }
    
    @Test
    public void testSiteFixPricingValid() {
        CampingSite site = new CampingSite(CampingType.TENT, 4, 10000);
        campingSiteManager.AddCampingSite(site);
        
        campingSiteManager.SiteFixPricing(site, "12500.50");
        
        Assert.assertEquals("Price should be updated to 12500.50", 12500.50, site.getPrice(), 0.01);
    }
    
    @Test
    public void testSiteFixPricingInvalidInput() {
        CampingSite site = new CampingSite(CampingType.TENT, 4, 10000);
        campingSiteManager.AddCampingSite(site);
        
        campingSiteManager.SiteFixPricing(site, "invalid");
        
        // Price should remain unchanged
        Assert.assertEquals("Price should remain 10000", 10000, site.getPrice(), 0.01);
    }
    
    @Test
    public void testModifyStatus() {
        CampingSite site = new CampingSite(CampingType.TENT, 4, 10000);
        campingSiteManager.AddCampingSite(site);
        
        campingSiteManager.ModifyStatus(site, "Aktív");
        
        Assert.assertEquals("Status should be updated to Aktív", "Aktív", site.getStatus());
    }
    
    @Test
    public void testFindCampingSiteById() {
        CampingSite site = new CampingSite(CampingType.TENT, 4, 10000);
        campingSiteManager.AddCampingSite(site);
        
        CampingSite found = campingSiteManager.findCampingSiteById(site.getId());
        
        Assert.assertNotNull("Site should be found", found);
        Assert.assertEquals("Should find the correct site", site.getId(), found.getId());
    }
    
    @Test
    public void testSearchByCampingType() {
        campingSiteManager.CreateCampingSite(CampingType.TENT, 4, 10000);
        campingSiteManager.CreateCampingSite(CampingType.CARAVAN, 6, 15000);
        campingSiteManager.CreateCampingSite(CampingType.CABIN, 8, 20000);
        
        CampingSite searchCriteria = new CampingSite(CampingType.TENT, 0, 0.0);
        List<?> results = campingSiteManager.search(searchCriteria);
        
        Assert.assertEquals("Should find 1 TENT site", 1, results.size());
    }
    
    @Test
    public void testSearchByCapacity() {
        campingSiteManager.CreateCampingSite(CampingType.TENT, 4, 10000);
        campingSiteManager.CreateCampingSite(CampingType.CARAVAN, 6, 15000);
        campingSiteManager.CreateCampingSite(CampingType.CABIN, 6, 20000);
        
        CampingSite searchCriteria = new CampingSite(null, 6, 0.0);
        List<?> results = campingSiteManager.search(searchCriteria);
        
        Assert.assertEquals("Should find 2 sites with capacity 6", 2, results.size());
    }
}
