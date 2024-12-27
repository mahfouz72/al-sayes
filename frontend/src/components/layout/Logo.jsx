import { Link } from "react-router-dom";
import { Car } from "lucide-react";

export default function Logo() {
    return (
        <Link to="/dashboard" className="flex items-center space-x-2">
            <img src="/logo.png" alt="logo" className="h-12" />
        </Link>
    );
}
