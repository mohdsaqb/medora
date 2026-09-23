import React from 'react';
import { useLocation, useNavigate, useParams } from 'react-router-dom';

/**
 * Supplies the routing props that React Router 5 passed automatically.
 *
 * Router 6 removed withRouter because its replacements are hooks, and hooks
 * cannot be called from class components. Every component here is a class, so
 * this shim reads the hooks in a function wrapper and forwards the results.
 *
 * `history.push` is kept as an alias for `navigate` so existing call sites
 * continue to work unchanged.
 */
export function withRouter(Component) {
	function ComponentWithRouterProp(props) {
		const location = useLocation();
		const navigate = useNavigate();
		const params = useParams();

		const history = {
			push: navigate,
			replace: to => navigate(to, { replace: true }),
			goBack: () => navigate(-1),
		};

		return (
			<Component
				{...props}
				router={{ location, navigate, params }}
				history={history}
				location={location}
				match={{ params }}
			/>
		);
	}

	return ComponentWithRouterProp;
}

export default withRouter;
