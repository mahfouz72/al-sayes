import { Link, useLocation } from "react-router-dom";

export default function NavLink({ to, children, className = "" }) {
    const location = useLocation();
    const isActive = location.pathname === to;

    return (
        <Link
            to={to}
            className={`${className} ${
                isActive
                    ? "bg-gray-900 text-white"
                    : "text-gray-300 hover:bg-gray-700 hover:text-white"
            } rounded-md px-3 py-3 text-sm font-medium`}
        >
            {children}
        </Link>
    );
}
