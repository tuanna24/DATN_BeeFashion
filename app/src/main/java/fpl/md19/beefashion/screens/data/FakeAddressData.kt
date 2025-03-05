package fpl.md19.beefashion.screens.data

import fpl.md19.beefashion.models.AddressModel

object FakeAddressData {
    private val addressList = mutableListOf(
        AddressModel(
            id = "1",
            province = "Hà Nội",
            district = "Ba Đình",
            ward = "Phúc Xá",
            detail = "123 Đường Hoàng Hoa Thám"
        ),
        AddressModel(
            id = "2",
            province = "Hồ Chí Minh",
            district = "Quận 1",
            ward = "Bến Nghé",
            detail = "456 Đường Nguyễn Huệ"
        ),
        AddressModel(
            id = "3",
            province = "Đà Nẵng",
            district = "Hải Châu",
            ward = "Hòa Thuận",
            detail = "789 Đường Trần Phú"
        ),
        AddressModel(
            id = "4",
            province = "Hà Nội",
            district = "Ba Đình",
            ward = "Điện Biên",
            detail = "123 Phố Kim Mã"
        ),
        AddressModel(
            id = "5",
            province = "Cần Thơ",
            district = "Ninh Kiều",
            ward = "Tân An",
            detail = "567 Đường 30 Tháng 4"
        )
    )

    fun getFakeAddresses(): List<AddressModel> {
        return addressList.toList()
    }

    fun addAddress(address: AddressModel) {
        addressList.add(address)
    }

    fun deleteAddress(addressId: String) {
        addressList.removeAll { it.id == addressId }
    }

    fun getNextId(): String {
        return (addressList.size + 1).toString()
    }
}
