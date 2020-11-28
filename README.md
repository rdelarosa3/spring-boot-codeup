# spring-boot-codeup
#ACTIVE JDBC
This is a spring-boot webapp using activejdbc for ORM & Thymleaf for template management. 
<br/>
# setup
Add the required dependencies:
<br/>
<br/>
pom.xml

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
            <exclusions>
                <exclusion>
                    <groupId>org.junit.vintage</groupId>
                    <artifactId>junit-vintage-engine</artifactId>
                </exclusion>
            </exclusions>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-thymeleaf</artifactId>
        </dependency>
       

Add Javalite & sql dependencies:
<br/>
<br/>
 pom.xml

        <dependency>
            <groupId>org.javalite</groupId>
            <artifactId>activejdbc</artifactId>
            <version>2.4-j8</version>
        </dependency>
        <!-- https://mvnrepository.com/artifact/org.javalite/activejdbc-instrumentation -->
        <dependency>
            <groupId>org.javalite</groupId>
            <artifactId>activejdbc-instrumentation</artifactId>
            <version>2.4-j8</version>
        </dependency>
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>8.0.16</version>
        </dependency>
        <dependency>
            <groupId>org.slf4j</groupId>
            <artifactId>slf4j-simple</artifactId>
            <version>1.7.9</version>
        </dependency>

 Add the following javalite plugins to build:
 <br/>
 <br/>
  pom.xml
 
        <plugins>
             <plugin>
                 <groupId>org.springframework.boot</groupId>
                 <artifactId>spring-boot-maven-plugin</artifactId>
             </plugin>
             <plugin>
                 <groupId>org.javalite</groupId>
                 <artifactId>activejdbc-instrumentation</artifactId>
                 <version>2.4-j8</version>
                 <executions>
                     <execution>
                         <phase>process-classes</phase>
                         <goals>
                             <goal>instrument</goal>
                         </goals>
                     </execution>
                 </executions>
             </plugin>
             <plugin>
                 <groupId>org.javalite</groupId>
                 <artifactId>db-migrator-maven-plugin</artifactId>
                 <version>2.4-j8</version>
                 <configuration>
                     <driver>com.mysql.jdbc.Driver</driver>
                     <url>jdbc:mysql://localhost/active_jdbc_db</url>
                     <username>root</username>
                     <password>root</password>
                 </configuration>
                 <executions>
                     <execution>
                         <id>dev_migrations</id>
                         <phase>validate</phase>
                         <goals>
                             <goal>migrate</goal>
                         </goals>
                     </execution>
                 </executions>
                 <dependencies>
                     <dependency>
                         <groupId>mysql</groupId>
                         <artifactId>mysql-connector-java</artifactId>
                         <version>8.0.16</version>
                     </dependency>
                 </dependencies>
             </plugin>
         </plugins> 
         
Create database.properties file inside src/main/resources:
<br/>
<br/>
database.properties

      development.driver=com.mysql.cj.jdbc.Driver
      development.username=root
      development.password=root
      development.url=jdbc:mysql://localhost/active_jdbc_db
      
      
      development.test.driver=com.mysql.cj.jdbc.Driver
      development.test.username=root
      development.test.password=root
      development.test.url=jdbc:mysql://localhost/active_jdbc_db
      
      production.driver=com.mysql.cj.jdbc.Driver
      production.username=root
      production.password=root
      production.url=jdbc:mysql://localhost/active_jdbc_db

Create migration file<br>
At the root of your project execute/terminal:<br>
      
    mvn db-migrator:new -Dname=your_migration_filename
    
This will simply create a new empty text file:
    
    Created new migration: .../src/migrations/20201130213201_your_migration_filename.sql
    
where 2020113021320 is a timestamp that is a good indicator when this migration was created.

Write SQL:
Open this file with your text editor and add free hand SQL there:
    
    CREATE TABLE tablename (
      id  int(11) DEFAULT NULL auto_increment PRIMARY KEY,
      column_name VARCHAR(128),
      created_at DATETIME,
      updated_at DATETIME
    )
    
Create as many migration files as needed.<br>
Run Migration with hte following command:
    
    mvn db-migrator:migrate
    
Alternatively you can run 
      
    mvn clean install

The DBMigrator will trigger during a validate phase.
Your new migrations will be executed against target databases. This means you do not need to execute mvn db-migrator:migrate during a normal development process. 

Instrumentation of model classes to make connection with database table is required:

When creating your model class import the jdbc model and extend to class:

    import org.javalite.activejdbc.Model;
     
    public class Employee extends Model {}

Anytime a model Class is created/updated you must process the class to instrument them;
this can be done with the following commands

    mvn process-classes
    mvn activejdbc-instrumentation:instrument
    
May also require a maven clean install

    mvn clean install


Basic usage

    DBConfiguration.loadConfiguration("/database.properties");
    Base.open();
    Base.close;
    
    ***  or ****
   
    Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://host/organization", "user", "xxxxx");
    
    Base.close;
    
Example of controller use

    @Controller
    public class AdController {
        @GetMapping("/ads") @ResponseBody
        public String index(Model model){
            DBConfiguration.loadConfiguration("/database.properties");
            Base.open();
            List<Ad> ads = Ad.findAll();
            model.addAttribute("ads",ads);
            Base.close();
            return "ads/index";
        }
    
        @GetMapping("/ads/{id}")
        public String showAd(@PathVariable long id,Model model){
            DBConfiguration.loadConfiguration("/database.properties");
            Base.open();
            model.addAttribute("ad", Ad.findFirst("id =?",id));
            Base.close();
            return "ads/show";
        }
    
        @GetMapping("/ads/new")
        public String createAd(){
            return "ads/new";
        }
    
        @PostMapping("/ads/new")
        public String submitAd(@RequestParam Map<String, String> requestParams,Model model){
    
            Ad ad = new Ad();
            ad.fromMap(requestParams);
            DBConfiguration.loadConfiguration("/database.properties");
            Base.open();
            if(ad.save()){
                model.addAttribute("ad",ad);
                System.out.println(ad.getId());
                Base.close();
                return "redirect:/ads/"+ad.getId();
            }else{
                model.addAttribute("errors", ad.errors());
                Base.close();
                return "ads/new";
            }
        }
    
        @GetMapping("/ads/{id}/edit")
        public String createAd(@PathVariable(name ="id") long id, Model model){
            DBConfiguration.loadConfiguration("/database.properties");
            Base.open();
            model.addAttribute("ad", Ad.findFirst("id =?",id));
            Base.close();
            return "ads/edit";
        }
    
        @PostMapping("/ads/{id}/edit")
        public String submitAd(@PathVariable(name ="id") long id, Model model){
            Base.close();
            model.addAttribute("ad", Ad.findFirst("id =?",id));
            Base.close();
            return "ads/edit";
        }
    } 


   