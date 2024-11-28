import axios from "axios";
import { jwtDecode } from "jwt-decode";

const axiosInstance = axios.create({
  baseURL: "http://localhost:8080",
  headers: {
    "Content-Type": "application/json",
    "Accept": "application/json", 
  },
  withCredentials: true,
});

axiosInstance.interceptors.request.use(
  (config) => {
    if (!config.url.startsWith("/category") || config.method !== 'get') {
    const accessToken = localStorage.getItem("accessToken");
    if (accessToken) {
      config.headers.Authorization = `Bearer ${accessToken}`;
    }
  }
    return config;
  },
  (error) => Promise.reject(error)
);

const refreshTokens = async () => {
  try {
    const refreshToken = localStorage.getItem("refreshToken");
    if (!refreshToken) {
      throw new Error("No refresh token available");
    }

    console.log("Attempting to refresh token...");
    const response = await axios.post("http://localhost:8080/auth/refresh", {
      refreshToken,
    });

    const { accessToken, refreshToken: newRefreshToken } = response.data;

    console.log("Token refresh successful. New accessToken:", accessToken);
    localStorage.setItem("accessToken", accessToken);
    localStorage.setItem("refreshToken", newRefreshToken);

    return accessToken;
  } catch (error) {
    console.error("Token refresh failed:", error.response?.data || error.message);
    localStorage.removeItem("accessToken");
    localStorage.removeItem("refreshToken");
    window.location.href = "/login";
    throw error;
  }
};

axiosInstance.interceptors.response.use(
  (response) => response,
  async (error) => {
    const originalRequest = error.config;

    if (error.response?.status === 401 && !originalRequest._retry) {
      originalRequest._retry = true;

      try {
        const newAccessToken = await refreshTokens();

        originalRequest.headers.Authorization = `Bearer ${newAccessToken}`;
        return axiosInstance(originalRequest);
      } catch (refreshError) {
        console.error("Retry after token refresh failed:", refreshError);
        return Promise.reject(refreshError);
      }
    }

    return Promise.reject(error);
  }
);

export const getUserId = () => {
  const accessToken = localStorage.getItem("accessToken");
  if (accessToken) {
    try {
      const decodedToken = jwtDecode(accessToken);
      return decodedToken.sub || decodedToken.userId; 
    } catch (error) {
      console.error("Error decoding token:", error);
    }
  }
  return null; 
};


export default axiosInstance;
