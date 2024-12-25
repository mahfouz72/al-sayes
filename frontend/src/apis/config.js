const config = {
    BASE_API_URL: 'http://localhost:8080/api',
  };
  
  // Function to get the token from localStorage
const getUserToken = () => {
  return localStorage.getItem('token');
};


export { config, getUserToken };

  