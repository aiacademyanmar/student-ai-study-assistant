package asia.aaii.studentstudy;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
/** V2.24: 48 new ORIGINAL MCQs. Totals 144 starter questions for 24 tracks.
 * Difficulty is editorially estimated, not validated psychometric calibration.
 */
public final class OfflineQuizCatalog {
 private OfflineQuizCatalog(){}
 public static final String ALL="All Topics",ANY="All Levels";
 public static final OfflineQuestionBank.Question[] NEW=new OfflineQuestionBank.Question[]{
    new OfflineQuestionBank.Question("q097","O","1123","Reading comprehension","The sign reads 'Library closes at 5 pm on Fridays'. When does it close on Fridays?",new String[]{"At 4 pm","At 5 pm","At 6 pm","It does not say"},1,"The time is explicitly specified as 5 pm."),
    new OfflineQuestionBank.Question("q098","O","1123","Language analysis","'The rain hammered on the roof.' Which technique is most obvious?",new String[]{"Personification","Metaphor","Onomatopoeia","Understatement"},0,"'Hammered' gives the rain a forceful human-like action; it personifies the rain."),
    new OfflineQuestionBank.Question("q099","O","4024","Algebra","Solve x²−9=0. What are the roots?",new String[]{"Only 3","Only −3","3 and −3","9 and −9"},2,"(x−3)(x+3)=0, so x=±3."),
    new OfflineQuestionBank.Question("q100","O","4024","Geometry","An exterior angle of a regular polygon is 45°. How many sides?",new String[]{"6","7","8","9"},2,"The number of sides is 360°/45°=8."),
    new OfflineQuestionBank.Question("q101","O","4037","Logarithmic functions","If log₂ x=5, find x.",new String[]{"10","16","25","32"},3,"By definition x=2⁵=32."),
    new OfflineQuestionBank.Question("q102","O","4037","Differentiation","For f(x)=x³−3x, identify the stationary x-coordinates.",new String[]{"−1 and 1","0 and 3","1 and 3","Only 0"},0,"f'(x)=3x²−3=0, hence x=−1 or 1."),
    new OfflineQuestionBank.Question("q103","O","5090","Enzymes","What generally happens when an enzyme is heated far above its optimum temperature?",new String[]{"It always speeds up","Its active site may lose shape","It becomes a new element","It stops being a protein"},1,"High temperature can denature the enzyme and change its active site."),
    new OfflineQuestionBank.Question("q104","O","5090","Inheritance","In a monohybrid cross Aa×aa, with A fully dominant, what fraction shows the recessive phenotype?",new String[]{"0","1/4","1/2","3/4"},2,"Half the offspring are aa, showing the recessive phenotype."),
    new OfflineQuestionBank.Question("q105","O","5070","Electrolysis","During electrolysis, positively charged ions move towards the:",new String[]{"Cathode","Anode","Salt bridge","Power switch"},0,"Cations are attracted to the negative cathode."),
    new OfflineQuestionBank.Question("q106","O","5070","Stoichiometry","Mg + 2HCl → MgCl₂ + H₂. How many moles of HCl react with 0.5 mol Mg?",new String[]{"0.25","0.5","1","2"},2,"Two moles HCl are required per mole Mg: 2×0.5=1."),
    new OfflineQuestionBank.Question("q107","O","5054","Momentum","A 2 kg object travels at 3 m/s. Its momentum is:",new String[]{"1.5 kg m/s","5 kg m/s","6 kg m/s","9 kg m/s"},2,"p=mv=2×3=6 kg m/s."),
    new OfflineQuestionBank.Question("q108","O","5054","Circuit power","A device operates at 12 V and 2 A. Its power is:",new String[]{"6 W","10 W","14 W","24 W"},3,"P=VI=12×2=24 W."),
    new OfflineQuestionBank.Question("q109","O","2210","Logic","What is NOT(true)?",new String[]{"true","false","one","unknown"},1,"NOT reverses Boolean values."),
    new OfflineQuestionBank.Question("q110","O","2210","Binary arithmetic","What is binary 1011 in decimal?",new String[]{"9","10","11","13"},2,"1011₂ = 8+0+2+1=11₁₀."),
    new OfflineQuestionBank.Question("q111","O","7115","Operations","A factory makes 120 units using 6 workers. Average output per worker is:",new String[]{"6","20","30","720"},1,"Labour productivity=120/6=20 units per worker."),
    new OfflineQuestionBank.Question("q112","O","7115","Business finance","A business has sales $800 and variable costs $500. Contribution is:",new String[]{"$300","$500","$800","$1300"},0,"Sales less variable costs equals contribution: 800−500=300."),
    new OfflineQuestionBank.Question("q113","O","2281","Markets","Which event shifts the demand curve for umbrellas rightward?",new String[]{"Umbrella production becomes cheaper","Consumers expect a very rainy season","Umbrella price rises alone","Factories produce more umbrellas"},1,"Expectations of rain can increase demand at every price."),
    new OfflineQuestionBank.Question("q114","O","2281","Elasticity","Price rises from 10 to 12 and quantity demanded falls from 100 to 90. Using original values, |PED| is:",new String[]{"0.25","0.5","1","2"},1,"Quantity −10%; price +20%; magnitude=10/20=0.5."),
    new OfflineQuestionBank.Question("q115","O","7707","Financial statements","Accounts receivable are usually classified as:",new String[]{"Current assets","Current liabilities","Capital","Long-term loans"},0,"Amounts due from customers are normally current assets."),
    new OfflineQuestionBank.Question("q116","O","7707","Depreciation","Equipment cost 6000, residual value 1000, life 5 years. Annual straight-line depreciation?",new String[]{"200","800","1000","1200"},2,"(6000−1000)/5=1000 per year."),
    new OfflineQuestionBank.Question("q117","O","2147","Chronology","What does 'chronological order' mean?",new String[]{"By alphabetical place","By importance only","By date/time sequence","By source length"},2,"Chronology orders events by time."),
    new OfflineQuestionBank.Question("q118","O","2147","Source evaluation","Two independent sources agree on an event. What is the best conclusion?",new String[]{"Their agreement can strengthen a claim, but context still matters","They must both be unbiased","One source can be discarded automatically","The claim is always historically proven"},0,"Corroboration is useful but provenance and context still need evaluation."),
    new OfflineQuestionBank.Question("q119","O","2217","Population","Birth rate 20 and death rate 8 per 1000. Natural increase per 1000?",new String[]{"8","12","20","28"},1,"Natural increase=20−8=12 per 1000."),
    new OfflineQuestionBank.Question("q120","O","2217","Map skills","On a 1:50,000 map, 2 cm represents:",new String[]{"100 m","500 m","1 km","10 km"},2,"2cm×50000=100000cm=1 km."),
    new OfflineQuestionBank.Question("q121","A","9093","Language change","What is a neologism?",new String[]{"A newly created word or expression","A silent letter","A sentence lacking a verb","A historical date"},0,"A neologism is a newly coined term or expression."),
    new OfflineQuestionBank.Question("q122","A","9093","Text analysis","'Despite the setbacks, we persevered.' The word 'despite' chiefly signals:",new String[]{"A concession or contrast","A numerical comparison","A direct quotation","An instruction"},0,"Despite indicates an expected obstacle that did not prevent the outcome."),
    new OfflineQuestionBank.Question("q123","A","9709","Pure mathematics","Solve 2x²−8=0.",new String[]{"−2 and 2","0 and 4","Only 2","−4 and 4"},0,"2(x²−4)=0, hence x=±2."),
    new OfflineQuestionBank.Question("q124","A","9709","Integration","Evaluate ∫₁³ 2x dx.",new String[]{"4","6","8","10"},2,"[x²]₁³=9−1=8."),
    new OfflineQuestionBank.Question("q125","A","9231","Matrices","If A is a 2×2 identity matrix, what is det(A)?",new String[]{"0","1","2","4"},1,"The determinant of an identity matrix is 1."),
    new OfflineQuestionBank.Question("q126","A","9231","Complex numbers","Find the modulus of 3+4i.",new String[]{"4","5","7","25"},1,"Modulus=√(3²+4²)=5."),
    new OfflineQuestionBank.Question("q127","A","9700","Cell respiration","Where does the Krebs cycle occur in a typical eukaryotic cell?",new String[]{"Cytosol","Mitochondrial matrix","Ribosome","Golgi apparatus"},1,"The Krebs cycle runs in the mitochondrial matrix."),
    new OfflineQuestionBank.Question("q128","A","9700","Genetic inheritance","For AaBb × AaBb with independent assortment and complete dominance, fraction with both dominant phenotypes?",new String[]{"3/16","9/16","1/2","3/4"},1,"Independent 3/4 dominant chances multiply: 3/4×3/4=9/16."),
    new OfflineQuestionBank.Question("q129","A","9701","pH calculation","For a strong monoprotic acid at [H⁺]=10⁻³ mol dm⁻³, approximate pH?",new String[]{"1","2","3","11"},2,"pH=−log₁₀[H⁺]=3."),
    new OfflineQuestionBank.Question("q130","A","9701","Equilibrium","For N₂ + 3H₂ ⇌ 2NH₃, increasing pressure generally favours:",new String[]{"Left (4 moles gas)","Right (2 moles gas)","Neither direction ever","Only the catalyst"},1,"Fewer gas moles on right: higher pressure tends to favour ammonia."),
    new OfflineQuestionBank.Question("q131","A","9702","Waves","A 5 Hz wave has wavelength 2 m. What is its speed?",new String[]{"2.5 m/s","7 m/s","10 m/s","25 m/s"},2,"v=fλ=5×2=10 m/s."),
    new OfflineQuestionBank.Question("q132","A","9702","Capacitance","A 4 µF capacitor at 10 V stores charge:",new String[]{"0.4 µC","4 µC","40 µC","400 µC"},2,"Q=CV=(4 µF)(10 V)=40 µC."),
    new OfflineQuestionBank.Question("q133","A","9618","Programming complexity","Binary search on sorted input typically has time complexity:",new String[]{"O(1)","O(log n)","O(n²)","O(2ⁿ)"},1,"Binary search halves the search range every step, giving logarithmic complexity."),
    new OfflineQuestionBank.Question("q134","A","9618","Stack operations","A stack receives 1, 2, 3 in that order; the first pop returns:",new String[]{"1","2","3","None"},2,"Stacks are LIFO; last inserted (3) is removed first."),
    new OfflineQuestionBank.Question("q135","A","9609","Business growth","Economies of scale usually reduce:",new String[]{"Average unit costs as scale expands","Total output to zero","Product quality necessarily","Marketing always"},0,"Scale advantages can lower average unit cost."),
    new OfflineQuestionBank.Question("q136","A","9609","Finance ratios","Current assets 90 and inventories 30, current liabilities 30. Acid-test ratio?",new String[]{"1:1","2:1","3:1","4:1"},1,"Quick ratio=(90−30)/30=2:1."),
    new OfflineQuestionBank.Question("q137","A","9708","Market structure","In perfect competition, an individual firm is usually a:",new String[]{"Price maker","Price taker","Monopolist","Regulator"},1,"Firms accept the market price under perfect competition assumptions."),
    new OfflineQuestionBank.Question("q138","A","9708","Elasticity","A supply price elasticity of 0.4 means supply is:",new String[]{"Perfectly elastic","Price inelastic","Unit elastic","Perfectly inelastic"},1,"0.4<1: quantity supplied responds less than proportionately."),
    new OfflineQuestionBank.Question("q139","A","9706","Accrual accounting","Which concept recognises expenses in the accounting period they relate to?",new String[]{"Going concern","Accrual (matching)","Materiality only","Money measurement"},1,"Accrual accounting recognises expenses when incurred, not only when paid."),
    new OfflineQuestionBank.Question("q140","A","9706","Variance analysis","Standard material price $5/kg, actual $6/kg for 100 kg purchased. Price variance?",new String[]{"$100 adverse","$100 favourable","$500 adverse","$600 favourable"},0,"Actual price higher by $1×100=$100 adverse."),
    new OfflineQuestionBank.Question("q141","A","9489","Historical methods","Why compare primary and secondary sources?",new String[]{"To test and contextualise interpretations","To avoid checking evidence","Because dates do not matter","To pick the longer text"},0,"Comparing source types can highlight interpretation and evidence quality."),
    new OfflineQuestionBank.Question("q142","A","9489","Interpretations","A historian's causal claim is best challenged by:",new String[]{"Relevant contradictory evidence and reasoning","Personal insults","Changing the subject","Only counting words"},0,"Historical argumentation tests causal explanations against contextual evidence."),
    new OfflineQuestionBank.Question("q143","A","9990","Research ethics","What does the right to withdraw allow participants?",new String[]{"To stop taking part without undue penalty","To choose the result","To remove randomisation","To demand payment"},0,"Participants can leave a study voluntarily."),
    new OfflineQuestionBank.Question("q144","A","9990","Statistical inference","A p-value less than 0.05 (when α=0.05) usually leads researchers to:",new String[]{"Reject the null hypothesis under test assumptions","Prove the alternative with certainty","Prove causation","Ignore the sample size"},0,"p<α supports rejection of H₀, but is not proof and depends on test assumptions.")
 };
 public static List<OfflineQuestionBank.Question> all(){
  List<OfflineQuestionBank.Question> a=new ArrayList<>();
  Collections.addAll(a,OfflineQuestionBank.ALL);Collections.addAll(a,NEW);Collections.addAll(a,OfflineQuizV225Bank.ITEMS);return a;
 }
 public static String difficulty(OfflineQuestionBank.Question q){
  try{
   int id=Integer.parseInt(q.id.substring(1));
   if(id<=96){int at=(id-1)%4;return at==0||at==1?"Easy":at==2?"Medium":"Hard";}
   return id%2==1?"Medium":"Hard";
  }catch(Exception ignored){return "Medium";}
 }
 public static List<String> topics(String level,String code){
  List<String> results=new ArrayList<>();results.add(ALL);
  for(OfflineQuestionBank.Question q:all())if(q.level.equals(level)&&q.code.equals(code)&&!results.contains(q.topic))results.add(q.topic);
  return results;
 }
 public static int count(String level,String code,String topic,String diff){
  return filter(level,code,topic,diff).size();
 }
 public static List<OfflineQuestionBank.Question> filter(String level,String code,String topic,String diff){
  List<OfflineQuestionBank.Question> out=new ArrayList<>();
  for(OfflineQuestionBank.Question q:all()){
   if(!q.level.equals(level)||!q.code.equals(code))continue;
   if(topic!=null&&!ALL.equals(topic)&&!topic.equals(q.topic))continue;
   if(diff!=null&&!ANY.equals(diff)&&!diff.equals(difficulty(q)))continue;
   out.add(q);
  }return out;
 }
 public static List<OfflineQuestionBank.Question> choose(String level,String code,String topic,String diff,int limit){
  List<OfflineQuestionBank.Question> out=filter(level,code,topic,diff);
  Collections.shuffle(out);
  if(limit>0&&out.size()>limit)return new ArrayList<>(out.subList(0,limit));
  return out;
 }
}
