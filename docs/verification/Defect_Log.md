# Defect Triage and Retest Log

| Defect ID | Description | Severity | Fix Applied | Status |
|---|---|---|---|---|
| DEF-001 | Flyway SQL BOM Encoding crashed ApplicationContext | High | Rewrote V6 SQL using BOM-free UTF-8 encoding | FIXED & RETESTED |
| DEF-002 | Hikari DataSource URL context failure in isolated tests | High | Injected explicit H2 properties via @SpringBootTest | FIXED & RETESTED |
| DEF-003 | Migration dry-run accepted invalid rows missing 'Author' | Medium | Added strict validation for column length and empty strings | FIXED & RETESTED |