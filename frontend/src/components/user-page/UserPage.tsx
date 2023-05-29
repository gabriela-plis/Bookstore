import { useEffect, useState } from "react";
import UserSidebar from "./Sidebar";
import { useLocation } from 'react-router-dom';
import useFetch from "../../functions/useFetch";
import User from "../../DTO/UserDTO";
import Details from "./Details";
import Settings from "./Settings";
import AddBookPanel from "./AddBookPanel";
import RemoveBookPanel from "./RemoveBookPanel";
import Borrows from "./Borrows";
import { USER_URL } from "../../variable/constants";

type Props = {
    setRenderLogoutText: React.Dispatch<React.SetStateAction<boolean>>;
}

const UserPage = (props: Props) => {

    type userUrlPrefixIsPresent = {
        "borrows": boolean,
        "details": boolean,
        "settings": boolean,
        "add-book": boolean,
        "remove-book": boolean
    }

    const [urlPrefixesPresence, setUrlPrefixesPresence] = useState<userUrlPrefixIsPresent>({
        "borrows": true,
        "details": false,
        "settings": false,
        "add-book": false,
        "remove-book":false
    })

    const location = useLocation();

    useEffect( () => {
        const currentUrl = location.pathname;
        const prefixOfCurrentUrl = currentUrl.substring("/user/".length)
        
        for (const urlPrefix in urlPrefixesPresence) {

            setUrlPrefixesPresence(prevState => (
                {
                    ...prevState,
                    [urlPrefix]: false
                }
            )) 

            if (urlPrefix === prefixOfCurrentUrl) {
                setUrlPrefixesPresence(prevState => (
                    {
                        ...prevState,
                        [urlPrefix]: true
                    }
                )) 
            }
        }

    }, [location])

    const user: User = useFetch(USER_URL) as unknown as User;
    const [render, setRender] = useState(false)

    const [isEmployee, setIsEmployee] = useState(false)

    useEffect( () => {
        if (!Array.isArray(user)) {
            setIsEmployee(user.roles.includes("EMPLOYEE"))
            setRender(true)
        }
    },[user])


    return ( 
    <main className="UserPage">
        {render && <UserSidebar employee={isEmployee} setRenderLogoutText={props.setRenderLogoutText}/>}
        {urlPrefixesPresence.borrows && <Borrows />}
        {urlPrefixesPresence.details && <Details />}
        {urlPrefixesPresence.settings && <Settings />}
        {urlPrefixesPresence["add-book"] && <AddBookPanel />}
        {urlPrefixesPresence["remove-book"] && <RemoveBookPanel />}
    </main> 
    );
}

export default UserPage;