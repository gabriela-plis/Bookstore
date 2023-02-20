
const logout = (setSessionIsActive: React.Dispatch<React.SetStateAction<boolean>>) => {
        sessionStorage.clear()
        setSessionIsActive(false);
}
 
export default logout;