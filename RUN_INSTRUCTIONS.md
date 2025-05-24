# Instructions d'Exécution - Injection de Dépendances

## Prérequis
1. Java 23 installé
2. Maven installé
3. Projet compilé avec `mvn clean compile`

## Partie 1 : Injection de Dépendances (TP Original)

### 1. Injection Statique
```bash
cd /Users/omar/IdeaProjects/ID-TP
mvn compile
java -cp target/classes net.omar.presentation.PresStatique
```

### 2. Injection Dynamique
```bash
# Assurez-vous que le fichier config.txt existe à la racine
java -cp target/classes net.omar.presentation.PresDynamique
```

### 3. Injection avec Spring XML
```bash
# Nécessite les dépendances Spring
mvn dependency:copy-dependencies
java -cp "target/classes:target/dependency/*" net.omar.presentation.PresSpringXML
```

### 4. Injection avec Spring Annotations
```bash
java -cp "target/classes:target/dependency/*" net.omar.presentation.PresSpringAnnotations
```

## Partie 2 : Mini Framework IoC

### 1. Exemple avec Annotations (Notre Framework)
```bash
java -cp "target/classes:target/dependency/*" net.omar.examples.presentation.AnnotationExample
```

### 2. Exemple avec XML (Notre Framework)
```bash
java -cp "target/classes:target/dependency/*" net.omar.examples.presentation.XmlExample
```

## Utilisation avec IntelliJ IDEA

1. **Ouvrir le projet**
   - File → Open → Sélectionner le dossier ID-TP

2. **Configurer le SDK**
   - File → Project Structure → Project SDK → Java 23

3. **Recharger les dépendances Maven**
   - Clic droit sur pom.xml → Maven → Reload project

4. **Exécuter les classes**
   - Naviguer vers src/main/java
   - Clic droit sur la classe → Run 'NomDeLaClasse.main()'

## Structure des Exemples

### Partie 1 - Classes de Présentation
- `PresStatique` : Injection manuelle avec `new`
- `PresDynamique` : Injection via réflexion (lit config.txt)
- `PresSpringXML` : Utilise Spring avec applicationContext.xml
- `PresSpringAnnotations` : Utilise Spring avec @Component/@Autowired

### Partie 2 - Framework Personnalisé
- `AnnotationExample` : Démontre notre framework avec annotations
- `XmlExample` : Démontre notre framework avec XML

## Résultats Attendus

### PresStatique
```
Version base de données
Résultat = [valeur calculée]
```

### PresDynamique
```
Version base de données
Résultat = [valeur calculée]
```

### AnnotationExample (Notre Framework)
```
=== Framework IoC - Example avec Annotations ===
UserService created with constructor injection
1. Test avec getBean par nom:
Finding user with id: 1
Service: User1
...
```

### XmlExample (Notre Framework)
```
=== Framework IoC - Example avec XML ===
MockDataSource created
Setting URL: jdbc:mysql://localhost:3306/test
...
```

## Dépannage

### Erreur: ClassNotFoundException
- Vérifier que `mvn compile` a été exécuté
- Vérifier le classpath

### Erreur: NoSuchFileException (config.txt)
- S'assurer que config.txt existe à la racine du projet
- Contenu attendu:
  ```
  net.omar.dao.DaoImpl
  net.omar.metier.MetierImpl
  ```

### Erreur: Spring version incompatible
- Spring 6.1.13 est nécessaire pour Java 23
- Vérifier pom.xml pour la version correcte

### Erreur: JAXB not found
- Les dépendances JAXB sont nécessaires pour le framework XML
- Vérifier que jakarta.xml.bind est dans pom.xml