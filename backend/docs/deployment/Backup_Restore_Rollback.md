# Backup, Restore, and Rollback Procedures

## Backup Before Migration
Run the following command before importing legacy records:
pg_dump -U admin aisys_library > backup_pre_migration.sql

## Rollback / Restore
If migration reconciliation fails or the database is corrupted:
1. Stop the application service.
2. Drop current schema: dropdb -U admin aisys_library
3. Recreate schema: createdb -U admin aisys_library
4. Restore: psql -U admin aisys_library < backup_pre_migration.sql
5. Restart application service.