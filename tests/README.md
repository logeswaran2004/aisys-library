# Tests

The SOP placeholder folders (`e2e`, `integration`, `performance`, `security`) are retained for document traceability.

Executable tests are in `backend/src/test/java`:

- Integration / journeys: `CirculationJourneyTest`, `RfidMiddlewareTest`, `migration/MigrationDatabaseTest`
- Performance: `verification/PerformanceVerificationTest`
- Security / RBAC: `verification/SecurityVerificationTest`

Run: `cd backend && mvn test`
