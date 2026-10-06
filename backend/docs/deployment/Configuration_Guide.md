# Configuration Guide
System configurations are managed externally to keep code clean.
1. Locate pplication-offline.yml in the installation directory.
2. Set spring.datasource.url to the local database instance.
3. Define RFID middleware IP boundaries under isys.rfid.gateways.
4. Ensure no plaintext secrets are stored in this file.