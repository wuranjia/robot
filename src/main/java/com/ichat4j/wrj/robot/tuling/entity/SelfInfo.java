package com.ichat4j.wrj.robot.tuling.entity;

public class SelfInfo {

    private location location;

    public SelfInfo.location getLocation() {
        return location;
    }

    public void setLocation(SelfInfo.location location) {
        this.location = location;
    }

    public class location{
        private String city;
        private String province;
        private String street;

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getStreet() {
            return street;
        }

        public void setStreet(String street) {
            this.street = street;
        }
    }
}
