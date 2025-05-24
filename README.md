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
