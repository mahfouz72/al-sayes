import { Link, useLocation } from "react-router-dom";

export default function NavLink({ to, children }) {
    const location = useLocation();
    const isActive = location.pathname === to;

    return (
        <Link
            to={to}
            className={`${
                isActive
                    ? "bg-blue-600 text-white"
                    : "text-gray-300 hover:bg-gray-700 hover:text-white"
            } px-4 py-2 rounded-lg text-sm font-medium transition-colors duration-150 ease-in-out`}
        >
            {children}
        </Link>
    );
}
