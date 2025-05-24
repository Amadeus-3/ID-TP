# TP Injection de Dépendances et Inversion de Contrôle

## Introduction
Ce projet illustre les concepts d'injection de dépendances et d'inversion de contrôle (IoC) en Java, en utilisant différentes approches : manuelle, dynamique et avec le framework Spring.

## Architecture du Projet

### Structure des Packages
```
src/main/java/net/omar/
├── dao/
│   ├── IDao.java         # Interface pour la couche DAO
│   └── DaoImpl.java      # Implémentation de l'interface IDao
├── metier/
│   ├── IMetier.java      # Interface pour la couche métier
│   └── MetierImpl.java   # Implémentation avec couplage faible
└── presentation/
    ├── PresStatique.java      # Injection par instanciation statique
    ├── PresDynamique.java     # Injection par instanciation dynamique
    ├── PresSpringXML.java     # Injection via Spring XML
    └── PresSpringAnnotations.java # Injection via annotations Spring
```

## Concepts Implémentés

### 1. Couplage Faible
- **Interface IDao** : Définit le contrat pour l'accès aux données
- **Interface IMetier** : Définit le contrat pour la logique métier
- La classe `MetierImpl` dépend de l'interface `IDao` et non de l'implémentation concrète

### 2. Injection de Dépendances

#### a) Instanciation Statique
```java
DaoImpl dao = new DaoImpl();
MetierImpl metier = new MetierImpl();
metier.setDao(dao);
```
- Création manuelle des instances
- Injection via setter
- Couplage fort dans la classe de présentation

#### b) Instanciation Dynamique
```java
Class cDao = Class.forName(daoClassName);
IDao dao = (IDao) cDao.newInstance();
```
- Utilisation de la réflexion Java
- Configuration via fichier `config.txt`
- Permet de changer l'implémentation sans recompilation

#### c) Framework Spring - Version XML
```xml
<bean id="dao" class="net.omar.dao.DaoImpl"></bean>
<bean id="metier" class="net.omar.metier.MetierImpl">
    <property name="dao" ref="dao"></property>
</bean>
```
- Configuration déclarative dans `applicationContext.xml`
- Spring gère le cycle de vie des objets

#### d) Framework Spring - Version Annotations
```java
@Component("dao")
public class DaoImpl implements IDao { ... }

@Component("metier")
public class MetierImpl implements IMetier {
    @Autowired
    private IDao dao;
    ...
}
```
- Configuration par annotations
- Scan automatique des composants
- Injection automatique des dépendances

## Exécution du Projet

### Prérequis
- Java 23
- Maven
- Spring Framework 6.1.13

### Compilation
```bash
mvn clean compile
```

### Exécution des Différentes Versions

1. **Version Statique**
   ```bash
   java -cp target/classes net.omar.presentation.PresStatique
   ```

2. **Version Dynamique**
   ```bash
   java -cp target/classes net.omar.presentation.PresDynamique
   ```
   Note : Nécessite le fichier `config.txt` à la racine du projet

3. **Version Spring XML**
   ```bash
   java -cp "target/classes:target/dependency/*" net.omar.presentation.PresSpringXML
   ```

4. **Version Spring Annotations**
   ```bash
   java -cp "target/classes:target/dependency/*" net.omar.presentation.PresSpringAnnotations
   ```

## Avantages de l'Injection de Dépendances

1. **Couplage Faible** : Les classes dépendent d'interfaces, pas d'implémentations
2. **Testabilité** : Facilite les tests unitaires avec des mocks
3. **Flexibilité** : Changement d'implémentation sans modifier le code
4. **Réutilisabilité** : Les composants peuvent être réutilisés dans différents contextes
5. **Maintenance** : Code plus propre et plus facile à maintenir

## Evolution du Projet

### Phase 1 : Structure de Base
- Création des interfaces IDao et IMetier
- Implémentation basique avec couplage fort

### Phase 2 : Couplage Faible
- Introduction du setter dans MetierImpl
- Séparation des responsabilités

### Phase 3 : Injection Dynamique
- Utilisation de la réflexion
- Configuration externalisée

### Phase 4 : Framework Spring
- Gestion automatique des dépendances
- Configuration XML puis annotations
- Inversion de contrôle complète

## Conclusion
Ce projet démontre l'évolution d'une application depuis un couplage fort vers une architecture découplée utilisant l'injection de dépendances. L'utilisation de Spring Framework simplifie grandement la gestion des dépendances et améliore la maintenabilité du code.

---

# Mini Framework IoC (Partie 2)

## Introduction
Ce mini framework d'injection de dépendances reproduit les fonctionnalités essentielles de Spring IoC, permettant l'injection de dépendances via XML et annotations.

## Architecture du Framework

### Structure des Packages
```
src/main/java/net/omar/framework/ioc/
├── annotations/
│   ├── Component.java      # Marque une classe comme composant géré
│   ├── Autowired.java      # Injection automatique de dépendances
│   └── Qualifier.java      # Spécifie quel bean injecter
├── core/
│   ├── ApplicationContext.java              # Interface principale
│   ├── AbstractApplicationContext.java      # Implémentation de base
│   ├── AnnotationConfigApplicationContext.java  # Contexte pour annotations
│   ├── XmlApplicationContext.java          # Contexte pour XML
│   └── BeanDefinition.java                 # Métadonnées des beans
├── xml/
│   ├── Beans.java          # Racine XML (JAXB)
│   ├── Bean.java           # Définition d'un bean
│   ├── Property.java       # Propriété d'un bean
│   └── ConstructorArg.java # Argument de constructeur
├── exceptions/
│   ├── BeanCreationException.java
│   ├── NoSuchBeanException.java
│   └── CircularDependencyException.java
└── utils/
    └── ClassScanner.java   # Scan des packages
```

## Fonctionnalités Implémentées

### 1. Injection par Annotations

#### @Component
```java
@Component("userDao")
public class UserDaoImpl implements UserDao {
    // ...
}
```

#### @Autowired
- **Field Injection**
```java
@Autowired
private UserDao userDao;
```

- **Constructor Injection**
```java
@Autowired
public UserServiceImpl(UserDao userDao) {
    this.userDao = userDao;
}
```

- **Setter Injection**
```java
@Autowired
public void setUserDao(UserDao userDao) {
    this.userDao = userDao;
}
```

#### @Qualifier
```java
@Autowired
@Qualifier("userDao")
private UserDao userDao;
```

### 2. Injection par XML

#### Configuration XML
```xml
<beans>
    <!-- Injection par setter -->
    <bean id="userService" class="com.example.UserServiceImpl">
        <property name="userDao" ref="userDao"/>
    </bean>
    
    <!-- Injection par constructeur -->
    <bean id="userService2" class="com.example.UserServiceImpl">
        <constructor-arg index="0" ref="userDao"/>
    </bean>
    
    <!-- Propriétés simples -->
    <bean id="dataSource" class="com.example.DataSource">
        <property name="url" value="jdbc:mysql://localhost:3306/db"/>
        <property name="maxConnections" value="10"/>
    </bean>
</beans>
```

### 3. Utilisation du Framework

#### Avec Annotations
```java
ApplicationContext context = new AnnotationConfigApplicationContext("com.example");
UserService service = context.getBean(UserService.class);
```

#### Avec XML
```java
ApplicationContext context = new XmlApplicationContext("classpath:beans.xml");
UserService service = (UserService) context.getBean("userService");
```

## Caractéristiques Techniques

### Gestion des Beans
- **Singleton par défaut** : Une seule instance par bean
- **Détection de dépendances circulaires**
- **Résolution automatique par type ou par nom**

### Types d'Injection Supportés
1. **Field Injection** : Accès direct aux champs privés via réflexion
2. **Setter Injection** : Via méthodes setter
3. **Constructor Injection** : Via constructeur annoté

### Conversion de Types
- Conversion automatique pour types primitifs
- Support des références entre beans
- Injection de valeurs depuis XML

## Différences avec Spring IoC

| Fonctionnalité | Notre Framework | Spring IoC |
|----------------|-----------------|------------|
| Annotations de base | ✅ | ✅ |
| Configuration XML | ✅ (JAXB) | ✅ |
| Injection Field/Setter/Constructor | ✅ | ✅ |
| Scopes | ❌ (singleton only) | ✅ |
| AOP | ❌ | ✅ |
| Événements | ❌ | ✅ |
| Profiles | ❌ | ✅ |

## Exemples d'Utilisation

Le framework inclut des exemples complets dans le package `net.omar.examples` :
- `AnnotationExample.java` : Démo avec annotations
- `XmlExample.java` : Démo avec configuration XML
- Implémentations DAO et Service pour les tests

## Points Clés d'Apprentissage

1. **Réflexion Java** : Utilisation intensive pour l'instantiation et l'injection
2. **JAXB** : Mapping objet-XML pour la configuration
3. **Design Patterns** : Factory, Singleton, Dependency Injection
4. **Architecture modulaire** : Séparation claire des responsabilités
