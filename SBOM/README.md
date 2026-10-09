# Software Bill of Materials

`cyclonedx-sbom.json` lists third-party libraries declared by `backend/pom.xml` (Spring Boot 3.2.5 parent BOM plus JWT). All are used for synthetic/offline development; regenerate after dependency changes with:

```bash
cd backend
mvn dependency:tree
```
