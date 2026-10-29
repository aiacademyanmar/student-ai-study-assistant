package asia.aaii.studentstudy;
/** Native V2.42, app-authored study taxonomy (not official university curricula). */
public final class SpecializationCatalog {
 private SpecializationCatalog(){}
 public static final String[] GROUPS={"University","Engineering","Medicine","Business"};
 public static final int[] TARGET={26,14,15,12};
 public static final class Subject{
  public final String group,name; public final String[] topics;
  Subject(String group,String name,String[] topics){this.group=group;this.name=name;this.topics=topics;}
 }
 public static final Subject[] ALL={
  new Subject("University","Physics",new String[]{"Mechanics","Electricity and Magnetism","Thermal Physics","Optics and Waves","Quantum Concepts"}),
  new Subject("University","Chemistry",new String[]{"Atomic Structure","Chemical Bonding","Chemical Equilibrium","Organic Reactions","Analytical Methods"}),
  new Subject("University","Mathematics",new String[]{"Calculus","Linear Algebra","Differential Equations","Discrete Mathematics","Real Analysis"}),
  new Subject("University","Statistics",new String[]{"Probability","Sampling and Estimation","Hypothesis Testing","Regression","Experimental Design"}),
  new Subject("University","Botany",new String[]{"Plant Cells","Plant Physiology","Taxonomy","Plant Ecology","Genetics in Plants"}),
  new Subject("University","Zoology",new String[]{"Animal Diversity","Animal Physiology","Developmental Biology","Ecology","Evolution"}),
  new Subject("University","Geology",new String[]{"Mineralogy","Petrology","Structural Geology","Sedimentology","Geological Mapping"}),
  new Subject("University","Geography",new String[]{"Physical Geography","Human Geography","GIS Fundamentals","Climate Systems","Field Methods"}),
  new Subject("University","History",new String[]{"Historical Methods","Southeast Asian History","Global History","Historiography","Primary Sources"}),
  new Subject("University","Economics",new String[]{"Microeconomic Theory","Macroeconomic Theory","Development Economics","Econometrics","Public Economics"}),
  new Subject("University","English Language",new String[]{"Academic Reading","Linguistics","Composition","Literary Analysis","Research Writing"}),
  new Subject("University","Myanmar Language",new String[]{"Myanmar Grammar","Academic Composition","Myanmar Literature","Text Analysis","Language Teaching"}),
  new Subject("University","Philosophy",new String[]{"Logic","Ethics","Epistemology","Philosophy of Science","Argument Analysis"}),
  new Subject("University","Psychology",new String[]{"Research Methods","Cognitive Psychology","Developmental Psychology","Social Psychology","Learning Theory"}),
  new Subject("University","Political Science",new String[]{"Political Institutions","Comparative Politics","Public Policy","Political Theory","Research Methods"}),
  new Subject("University","International Relations",new String[]{"Diplomacy","International Organizations","International Law Concepts","Global Security","Regional Cooperation"}),
  new Subject("University","Law",new String[]{"Legal Research","Contract Law Concepts","Legal Reasoning","Constitutional Principles","Legal Ethics"}),
  new Subject("University","Computer Science",new String[]{"Algorithms","Data Structures","Operating Systems","Databases","Computer Networks"}),
  new Subject("University","Information Technology",new String[]{"Web Systems","Network Administration","Cybersecurity Fundamentals","Information Systems","Cloud Concepts"}),
  new Subject("University","Environmental Science",new String[]{"Ecosystems","Pollution Monitoring","Sustainability","Environmental Impact","Conservation"}),
  new Subject("University","Education",new String[]{"Learning Theories","Curriculum Design","Assessment","Classroom Management","Educational Technology"}),
  new Subject("University","Sociology",new String[]{"Social Theory","Research Design","Social Institutions","Inequality","Community Studies"}),
  new Subject("University","Anthropology",new String[]{"Cultural Anthropology","Ethnographic Methods","Archaeological Anthropology","Language and Culture","Ethics of Fieldwork"}),
  new Subject("University","Archaeology",new String[]{"Survey Methods","Excavation Concepts","Artifact Analysis","Chronology","Heritage Preservation"}),
  new Subject("University","Agriculture",new String[]{"Soil Science","Crop Production","Plant Protection","Farm Management","Sustainable Agriculture"}),
  new Subject("University","Biochemistry",new String[]{"Biomolecules","Enzyme Kinetics","Metabolism","Molecular Techniques","Cell Signaling"}),
  new Subject("Engineering","Civil Engineering",new String[]{"Structural Analysis","Geotechnics","Hydraulics","Transportation","Construction Management"}),
  new Subject("Engineering","Mechanical Engineering",new String[]{"Statics and Dynamics","Thermodynamics","Fluid Mechanics","Machine Design","Manufacturing"}),
  new Subject("Engineering","Electrical Engineering",new String[]{"Circuit Analysis","Power Systems","Electrical Machines","Control Systems","Safety Standards"}),
  new Subject("Engineering","Electronic Engineering",new String[]{"Analog Circuits","Digital Electronics","Signal Processing","Microelectronics","Embedded Systems"}),
  new Subject("Engineering","Computer Engineering",new String[]{"Digital Logic","Computer Architecture","Embedded Programming","Interfacing","Hardware Verification"}),
  new Subject("Engineering","Software Engineering",new String[]{"Requirements","Software Architecture","Testing","Version Control","Deployment"}),
  new Subject("Engineering","Chemical Engineering",new String[]{"Mass Balance","Heat Transfer","Reaction Engineering","Separation Processes","Process Safety"}),
  new Subject("Engineering","Industrial Engineering",new String[]{"Operations Research","Quality Systems","Facility Planning","Ergonomics","Process Improvement"}),
  new Subject("Engineering","Architecture",new String[]{"Design Principles","Building Systems","Sustainable Design","Architectural Drawing","Urban Context"}),
  new Subject("Engineering","Aerospace Engineering",new String[]{"Aerodynamics","Flight Mechanics","Aircraft Structures","Propulsion Concepts","Aerospace Materials"}),
  new Subject("Engineering","Mechatronics",new String[]{"Sensors and Actuators","Robotics","Control Systems","Embedded Controllers","System Integration"}),
  new Subject("Engineering","Telecommunications Engineering",new String[]{"Communication Theory","Wireless Systems","Antennas","Network Protocols","Optical Networks"}),
  new Subject("Engineering","Biomedical Engineering",new String[]{"Biomechanics","Medical Instrumentation","Biomedical Signals","Design Validation","Medical Device Ethics"}),
  new Subject("Engineering","Renewable Energy Engineering",new String[]{"Solar Systems","Wind Systems","Energy Storage","Grid Integration","Energy Economics"}),
  new Subject("Medicine","Anatomy",new String[]{"Gross Anatomy","Neuroanatomy","Thorax and Abdomen","Musculoskeletal Anatomy","Clinical Anatomy Concepts"}),
  new Subject("Medicine","Physiology",new String[]{"Cell Physiology","Cardiovascular Physiology","Respiratory Physiology","Renal Physiology","Endocrine Physiology"}),
  new Subject("Medicine","Medical Biochemistry",new String[]{"Metabolism","Enzyme Function","Clinical Biomarkers","Nutrition Biochemistry","Molecular Biology"}),
  new Subject("Medicine","Histology",new String[]{"Epithelial Tissue","Connective Tissue","Muscle Tissue","Nervous Tissue","Organ Histology"}),
  new Subject("Medicine","Pathology",new String[]{"Cell Injury","Inflammation","Neoplasia","Organ Pathology","Laboratory Interpretation Concepts"}),
  new Subject("Medicine","Pharmacology",new String[]{"Pharmacokinetics","Pharmacodynamics","Drug Classes","Adverse Effects Concepts","Safe Prescribing Principles"}),
  new Subject("Medicine","Medical Microbiology",new String[]{"Bacteriology","Virology","Mycology","Parasitology","Infection Prevention"}),
  new Subject("Medicine","Immunology",new String[]{"Innate Immunity","Adaptive Immunity","Antibodies","Vaccines","Immunopathology Concepts"}),
  new Subject("Medicine","Community Medicine",new String[]{"Epidemiology","Health Promotion","Screening Concepts","Health Systems","Community Assessment"}),
  new Subject("Medicine","Public Health",new String[]{"Biostatistics","Population Health","Environmental Health","Health Policy","Outbreak Investigation Concepts"}),
  new Subject("Medicine","Internal Medicine",new String[]{"Clinical Reasoning Concepts","Cardiovascular Conditions","Respiratory Conditions","Endocrine Conditions","Patient Safety"}),
  new Subject("Medicine","Surgery",new String[]{"Surgical Principles","Perioperative Care","Wound Healing","Sterile Practice","Surgical Ethics"}),
  new Subject("Medicine","Pediatrics",new String[]{"Child Growth","Developmental Milestones","Common Pediatric Conditions","Vaccination Principles","Family Communication"}),
  new Subject("Medicine","Obstetrics and Gynecology",new String[]{"Reproductive Anatomy","Pregnancy Physiology","Antenatal Care Concepts","Women's Health","Maternal Safety"}),
  new Subject("Medicine","Nursing Science",new String[]{"Nursing Foundations","Patient Communication","Basic Assessment","Infection Control","Care Documentation"}),
  new Subject("Business","Principles of Management",new String[]{"Planning","Organizing","Leading","Controlling","Ethics"}),
  new Subject("Business","Marketing",new String[]{"Market Research","Segmentation","Marketing Mix","Digital Marketing","Campaign Evaluation"}),
  new Subject("Business","Accounting",new String[]{"Financial Statements","Cost Accounting","Budgeting","Internal Control","Accounting Ethics"}),
  new Subject("Business","Corporate Finance",new String[]{"Time Value of Money","Capital Budgeting","Risk and Return","Working Capital","Financial Analysis"}),
  new Subject("Business","Human Resource Management",new String[]{"Recruitment","Training","Performance Management","Compensation","Labor Relations"}),
  new Subject("Business","Operations Management",new String[]{"Capacity Planning","Process Design","Quality","Inventory","Service Operations"}),
  new Subject("Business","Supply Chain Management",new String[]{"Procurement","Logistics","Inventory Networks","Supplier Relationships","Risk Management"}),
  new Subject("Business","Entrepreneurship",new String[]{"Idea Validation","Business Models","Customer Interviews","Startup Finance","Pitching"}),
  new Subject("Business","Project Management",new String[]{"Scope","Scheduling","Cost","Risk","Stakeholder Management"}),
  new Subject("Business","Business Analytics",new String[]{"Data Cleaning","Descriptive Analytics","Data Visualization","Forecasting","Decision Support"}),
  new Subject("Business","Organizational Behavior",new String[]{"Motivation","Team Dynamics","Leadership","Culture","Conflict Resolution"}),
  new Subject("Business","Strategic Management",new String[]{"External Analysis","Competitive Advantage","Strategy Formulation","Implementation","Strategic Evaluation"})
 };
 public static Subject find(String group,String name){
  for(Subject s:ALL)if(s.group.equals(group)&&s.name.equals(name))return s;
  return null;
 }
 public static int countGroup(String group){int n=0;for(Subject s:ALL)if(s.group.equals(group))n++;return n;}
 public static void validate(){
  if(ALL.length!=67)throw new IllegalStateException("Expected 67 specialization subjects");
  for(int i=0;i<GROUPS.length;i++)if(countGroup(GROUPS[i])!=TARGET[i])throw new IllegalStateException(GROUPS[i]);
  for(Subject s:ALL)if(s.topics.length!=5)throw new IllegalStateException(s.name);
 }
}
