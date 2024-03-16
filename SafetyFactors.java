import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.FileWriter;

public class SafetyFactors{
    static double case_od;
    static double case_thick;
    static double case_id;
    static double meop;
    static double closure_load;
    public static void main(String[] args) throws Exception{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(new FileWriter("sf.txt"));

        System.out.println("Enter MEOP: ");
        meop = Double.parseDouble(br.readLine());


        System.out.println("Enter Casing OD: ");
        case_od = Double.parseDouble(br.readLine());

        System.out.println("Enter Casing Thickness: ");
        case_thick = Double.parseDouble(br.readLine());

        //Inner diameter:
        System.out.println("Enter Casing Inner Diameter: ");
        case_id = Double.parseDouble(br.readLine());

        System.out.println("Enter edge distance: "); 
        /**
         * Distance from where the hole starts to the edge of the casing 
         * As opposed to distance b/ center of whole and casing edge
         * 
        */
        double edge_dist = Double.parseDouble(br.readLine());

        System.out.println("Enter number of bolts: ");
        int num_bolt = Integer.parseInt(br.readLine());

        System.out.println("Enter major diameter of bolt: ");
        double bolt_major = Double.parseDouble(br.readLine());
        
        System.out.println("Enter minor diameter of bolt: ");
        double bolt_minor = Double.parseDouble(br.readLine());

        closure_load = Math.PI/4 * case_id * case_id * meop;



        /**Taking 6061 T6 as the casing material:
         * shear strength
         * yield tensile strength
         * bearing yield strength
        */

        br.close();
    }

    static double bolt_shear_SF(double bolt_shear_strength, int num_bolt, double bolt_minor){
        double stress = closure_load / (num_bolt * Math.PI/4 * bolt_minor * bolt_minor);

        return bolt_shear_strength / stress;
    }

    static double tear_out_SF(double casing_shear_strength, int num_bolt, double edge_dist, double case_thick){
        double casing_load = closure_load / num_bolt;
        double casing_shear_area = edge_dist * case_thick * 2; //load acts on two shear planes

        double shear_stress = casing_load / casing_shear_area;

        return casing_shear_strength / shear_stress;
    }

    static double tensile_SF(double yield_tensile_strength, double case_od, double case_id, double bolt_major, int num_bolt, double case_thick){
        double area = ((case_od - case_thick) * Math.PI - num_bolt * bolt_major) * case_thick; //conservative over estimate
        double stress = closure_load / area;

        return yield_tensile_strength / stress;
        
    }

    static double bearing_SF(double bearing_yield_strength, double case_thick, double bolt_major){ //acts on a single fastener
        double stress = closure_load / (case_thick * bolt_major);

        return bearing_yield_strength / stress;
    }

    static double hoop_SF(double yield_tensile_strength, double case_od, double case_thick){
        double stress = meop * (case_od - case_thick) / case_thick;

        return yield_tensile_strength / stress;
    }

    static double axial_SF(double yield_tensile_strength, double case_id, double case_thick){ //acts on closure, tensile load
        double stress = meop * case_id / (4*case_thick);

        return yield_tensile_strength / stress;
    }
}