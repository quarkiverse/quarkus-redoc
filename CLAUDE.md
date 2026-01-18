# Claude Code Rules

## Testing Preferences

- Prefer real implementations over mocks when feasible (e.g., use SmallRye Config with actual properties files instead of mocking config interfaces)
- Use built-in utilities over custom implementations (e.g., `PropertiesConfigSource` instead of custom `ConfigSource`)
- Use JUnit 5 parameterized tests (`@ParameterizedTest` with `@ValueSource`) to reduce code duplication
- Organize test resources in distinct directories per test case, with consistent file names:
  ```
  src/test/resources/<test-name>/<case>/
  ├── config.properties  (input)
  └── expected.json      (expected output)
  ```
- For JSON assertions, use JsonUnit (`json-unit-assertj`) for order-independent comparison and clear error messages

## Maven/Dependency Management

- Declare dependency versions as properties in the parent pom
- Manage dependencies in the parent pom's `dependencyManagement` section
- Child modules should not specify versions for managed dependencies

## General Code Style

- Prefer simple, idiomatic solutions over complex custom implementations
- Iteratively refine code through discussion rather than over-engineering upfront
