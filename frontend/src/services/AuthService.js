const STORAGE_KEY = 'medora.auth';

/**
 * Holds the signed in staff account for the lifetime of the browser tab.
 *
 * Credentials live in sessionStorage rather than localStorage so they are
 * dropped when the tab closes. The API uses HTTP Basic, so the password is
 * needed on every request and cannot be discarded after sign in.
 */
class AuthService {

    getSession() {
        const raw = sessionStorage.getItem(STORAGE_KEY);
        return raw ? JSON.parse(raw) : null;
    }

    signIn(username, password, role) {
        sessionStorage.setItem(STORAGE_KEY, JSON.stringify({ username, password, role }));
    }

    signOut() {
        sessionStorage.removeItem(STORAGE_KEY);
    }

    isSignedIn() {
        return this.getSession() !== null;
    }

    getRole() {
        const session = this.getSession();
        return session ? session.role : null;
    }

    /** Only admins may delete, matching the rules in SecurityConfig. */
    canDelete() {
        return this.getRole() === 'ADMIN';
    }

    /** Doctors and admins may edit diagnoses and prescriptions. */
    canEditClinical() {
        const role = this.getRole();
        return role === 'DOCTOR' || role === 'ADMIN';
    }

    /** Any signed in staff account may register and update patients. */
    canEditPatients() {
        return this.isSignedIn();
    }

    /** Value for the Authorization header, or null when signed out. */
    getAuthHeader() {
        const session = this.getSession();
        if (!session) {
            return null;
        }
        return 'Basic ' + btoa(session.username + ':' + session.password);
    }
}

const authService = new AuthService();
export default authService;
