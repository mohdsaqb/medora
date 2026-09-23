// Adds the custom DOM matchers used by the component tests.
import '@testing-library/jest-dom/vitest';
import { vi } from 'vitest';

/**
 * Stops components reaching the network during tests.
 *
 * Several components fetch on mount. Without this the suite only passes when a
 * backend happens to be running locally, and fails in CI with ERR_NETWORK
 * raised after the test environment has already torn down.
 *
 * Individual tests can still override a response with vi.mocked(...).
 */
vi.mock('axios', () => {
	const response = { data: [], status: 200, headers: {}, config: {} };
	const instance = {
		get: vi.fn(() => Promise.resolve(response)),
		post: vi.fn(() => Promise.resolve(response)),
		put: vi.fn(() => Promise.resolve(response)),
		delete: vi.fn(() => Promise.resolve(response)),
		interceptors: {
			request: { use: vi.fn() },
			response: { use: vi.fn() },
		},
	};

	return {
		default: { ...instance, create: vi.fn(() => instance) },
	};
});
